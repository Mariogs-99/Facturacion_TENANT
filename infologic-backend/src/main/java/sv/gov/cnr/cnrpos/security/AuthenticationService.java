package sv.gov.cnr.cnrpos.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import sv.gov.cnr.cnrpos.config.JwtService;
import sv.gov.cnr.cnrpos.config.DynamicDataSourceManager;
import sv.gov.cnr.cnrpos.config.TenantContext;
import sv.gov.cnr.cnrpos.entities.MasterCompany;
import sv.gov.cnr.cnrpos.entities.MasterUser;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.models.enums.TokenType;
import sv.gov.cnr.cnrpos.models.security.Token;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.repositories.MasterUserRepository;
import sv.gov.cnr.cnrpos.repositories.security.TokenRepository;
import sv.gov.cnr.cnrpos.repositories.security.UserRepository;
import sv.gov.cnr.cnrpos.services.MenuService;
import sv.gov.cnr.cnrpos.services.SucursalService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MasterUserRepository masterUserRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final MenuService menuService;
    private final SucursalService sucursalService;

    @PersistenceContext
    private EntityManager entityManager;



    // ! AUTHENTICATION DEL USUARIO CON COMPANY ID
    public Map<String, Object> authenticate(AuthenticationRequest request) {
        try {
            // 🔁 Aseguramos que se consulte en la base de datos 'master' al inicio
            TenantContext.setCurrentTenant("master");

            log.info("🔹 Iniciando autenticación para usuario: {}", request.getUsuario());

            MasterUser masterUser = masterUserRepository.findByUsuario(request.getUsuario())
                    .orElseThrow(() -> new BadCredentialsException("Usuario y/o contraseña incorrectos"));
            log.info("✅ Usuario encontrado en master: {}", masterUser.getUsuario());

            if (!passwordEncoder.matches(request.getClave(), masterUser.getClave())) {
                log.warn("❌ Contraseña incorrecta para usuario: {}", request.getUsuario());
                throw new BadCredentialsException("Usuario y/o contraseña incorrectos");
            }

            MasterCompany company = masterUser.getCompany();
            if (company == null || company.getDatabaseUrl() == null) {
                log.error("🚨 Empresa no válida o sin base de datos configurada");
                throw new BadCredentialsException("Empresa no válida o sin base de datos");
            }

            log.info("🏢 Empresa asignada: {} | Base de datos: {}", company.getNameCompany(), company.getDatabaseUrl());

            DynamicDataSourceManager.setDataSource(
                    company.getCompanyId().toString(),
                    company.getDatabaseUrl(),
                    company.getDatabaseUsername(),
                    company.getDatabasePassword()
            );

            TenantContext.setCurrentTenant(company.getCompanyId().toString());
            log.info("🔁 Conexión dinámica cambiada al tenant: {}", company.getCompanyId());

            entityManager.clear();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            log.info("🧼 EntityManager y caché limpiados para asegurar el uso del tenant actual");

            User user = userRepository.findByUsuario(request.getUsuario())
                    .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado en la empresa"));

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getIdUser());
            claims.put("username", user.getUsuario());
            claims.put("company_id", company.getCompanyId()); // ! Agregado companyID


            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsuario(),
                    "",
                    new ArrayList<>()
            );

            String jwtToken = jwtService.generateToken(claims, userDetails);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

            Map<String, Object> requeridos = new HashMap<>();
            if (user.getIdBranch() == null) requeridos.put("idBranch", true);
            if (user.getDocNumber() == null) requeridos.put("docNumber", true);

            Map<String, Object> userData = new HashMap<>();
            userData.put("reset_password", user.getResetPassword());
            userData.put("requerido_facturacion", requeridos);
            userData.put("username", user.getUsuario());
            userData.put("company_id", company.getCompanyId());
            userData.put("token", jwtToken);
            userData.put("menu", menuService.findMenuMenuItemsIdsByUserIdv2(user.getIdUser()));

            // Datos adicionales opcionales
            String codDepartamento = "80";
            if (user.getIdBranch() != null) {
                try {
                    codDepartamento = Optional.ofNullable(sucursalService.getSucursal(user.getIdBranch()))
                            .map(s -> String.valueOf(s.getIdDeptoBranch() != null ? s.getIdDeptoBranch() : 80))
                            .orElse("80");
                } catch (Exception e) {
                    log.warn("⚠️ No se pudo obtener sucursal para ID: {}", user.getIdBranch());
                }
            }

            userData.put("departamento", codDepartamento);
            userData.put("nombre", user.getFirstname().concat(" ").concat(user.getLastname()));
            userData.put("dui", user.getDocNumber() != null ? user.getDocNumber().replace("-", "") : "");

            log.info("✅ Autenticación completada para usuario: {}", user.getUsuario());
            return userData;

        } catch (BadCredentialsException bd) {
            log.warn("⚠️ Fallo de autenticación: {}", bd.getMessage());
            throw bd;
        } catch (Exception ex) {
            log.error("🚨 Error desconocido durante autenticación de {}: {}", request.getUsuario(), ex.getMessage(), ex);
            throw new BadCredentialsException("Error desconocido: " + ex.getMessage());
        }
    }


    // ? ----------------------------------------------------------------
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getIdUser());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public User loggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        return null;
    }

    // ? ----------------------------------------------------------------

    // ! LOGOUT DEL USER AL GENERAR TOKEN PRO TENANT CONTEXT
    public Token logout(String tokenString) {
        try {
            // 🔍 Asegurar que no tenga el prefijo "Bearer "
            if (tokenString.startsWith("Bearer ")) {
                tokenString = tokenString.substring(7);
            }

            // 🔐 Extraer company_id
            String tenantId = jwtService.extractClaim(tokenString, claims -> claims.get("company_id").toString());

            TenantContext.setCurrentTenant(tenantId);

            Token token = tokenRepository.findByToken(tokenString).orElse(null);
            if (token == null) {
                throw new ResourceNotFoundException("No se encontró el token");
            }

            token.setRevoked(true);
            token.setExpired(true);
            return tokenRepository.save(token);

        } catch (Exception ex) {
            log.error("❌ Error en logout: {}", ex.getMessage(), ex);
            throw new BadCredentialsException("Token inválido o sin company_id.");
        } finally {
            // 🧹 Siempre limpiar el tenant, incluso si hubo error
            TenantContext.clear();
            log.info("🧹 TenantContext limpiado en logout");
        }
    }






    // ! LOGOUT DEL TOKEN
    public void logoutByToken(String token) {
        Token tokenObj = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el token"));

        tokenObj.setRevoked(true);
        tokenObj.setExpired(true);
        tokenRepository.save(tokenObj);
    }


}