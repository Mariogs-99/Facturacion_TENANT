package sv.gov.cnr.cnrpos.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sv.gov.cnr.cnrpos.repositories.security.TokenRepository;

import java.io.IOException;
import java.util.Optional;

@Component
@Primary
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String userEmail;

        try {
            userEmail = jwtService.extractUserName(jwt);
        } catch (ExpiredJwtException ex) {
            handleAuthenticationFailure(response, "Token has expired", HttpStatus.UNAUTHORIZED);
            return;
        } catch (JwtException ex) {
            handleAuthenticationFailure(response, "Invalid token", HttpStatus.FORBIDDEN);
            return;
        }

        try {
            Object tenantClaim = jwtService.extractClaim(jwt, claims -> claims.get("company_id"));
            if (tenantClaim == null) {
                handleAuthenticationFailure(response, "Missing 'company_id' in token", HttpStatus.BAD_REQUEST);
                return;
            }

            String tenantId = tenantClaim.toString();
            TenantContext.setCurrentTenant(tenantId);

            // Cargar usuario y continuar con la autenticación
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            Optional<Boolean> isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked());

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid.orElse(false)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                handleAuthenticationFailure(response, "Token is invalid or expired", HttpStatus.UNAUTHORIZED);
                return;
            }

        } catch (Exception e) {
            handleAuthenticationFailure(response, "Error during authentication: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }


        filterChain.doFilter(request, response);
    }

    private void handleAuthenticationFailure(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
