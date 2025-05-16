package sv.gov.cnr.cnrpos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.models.dto.UserDTO;
import sv.gov.cnr.cnrpos.models.mappers.security.UserMapper;
import sv.gov.cnr.cnrpos.models.security.Rol;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.repositories.security.RolRepository;
import sv.gov.cnr.cnrpos.repositories.security.UserRepository;
import sv.gov.cnr.cnrpos.utils.RequestParamParser;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final RequestParamParser requestParamParser;
    private final UserMapper userMapper;
    private final long sisId;

    // Constructor con inyección de dependencias
    public UserService(
            UserRepository userRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            RequestParamParser requestParamParser,
            UserMapper userMapper,
            @Value("${cnrapps.SISID}") long sisId
    ) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.requestParamParser = requestParamParser;
        this.userMapper = userMapper;
        this.sisId = sisId;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public UserDTO getByUsername(String username) {
        User user = userRepository.findByUsuario(username).orElse(null);
        return userMapper.toDTO(user);
    }

    public UserDTO getUserDTO(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return userMapper.toDTO(user);
    }

    public Page<UserDTO> getPage(int page, int size, String filterBy, String sortBy) {
        Sort sort = sortBy.isEmpty() ? Sort.by("idUser").ascending() : requestParamParser.parseSortBy(sortBy);
        Map<String, String> filterParams = requestParamParser.parseFilterBy(filterBy);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users;

        if (!filterParams.isEmpty()) {
            users = userRepository.findAll(requestParamParser.withFilters(filterParams), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(userMapper::toPageDTO);
    }

    //Metodo generar carnet

    private String generarCarnet() {
        Random random = new Random();
        int numeroAleatorio = 1000 + random.nextInt(9000); // Genera un número entre 1000 y 9999
        return String.valueOf(numeroAleatorio);
    }

    public User saveUser(User user) throws RuntimeException, Exception {
        try {
            // Verificar si el correo ya está en uso
            List<User> emailExist = userRepository.findByEmailOverrided(user.getEmail());
            if (!emailExist.isEmpty()) {
                throw new ResourceNotFoundException("El correo seleccionado ya está en uso");
            }

            // Verificar si el usuario ya está en uso
            List<User> userExist = userRepository.findByUsuarioOverrided(user.getUsuario());
            if (!userExist.isEmpty()) {
                throw new ResourceNotFoundException("El usuario seleccionado ya está en uso");
            }

            // Generar carnet de 4 dígitos si no tiene uno
            if (user.getCarnet() == null || user.getCarnet().isEmpty()) {
                user.setCarnet(generarCarnet());
            }

            // Asignar roles si existen
            Set<Rol> roles = user.getRolIds() != null && !user.getRolIds().isEmpty()
                    ? new HashSet<>(rolRepository.findAllById(user.getRolIds()))
                    : null;

            if (roles != null) {
                user.setRoles(roles);
            } else {
                user.getRoles().clear();
            }

            // Codificar la contraseña antes de guardarla
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Guardar el usuario en la base de datos
            return userRepository.save(user);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        } catch (Exception exc) {
            throw new Exception("Error desconocido: " + exc.getMessage());
        }
    }

    public User updateUser(Long id, User userReq) throws RuntimeException, Exception {
        try {
            // Buscar el usuario existente por su ID
            User user = getUser(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró usuario con ID " + id));

            // Verificar si el correo ya está en uso por otro usuario
            if (!user.getEmail().equals(userReq.getEmail())) {
                List<User> emailExist = userRepository.findByEmailOverrided(userReq.getEmail());
                if (!emailExist.isEmpty()) {
                    throw new ResourceNotFoundException("El correo seleccionado ya está en uso");
                }
            }

            // Verificar si el usuario ya está en uso por otro usuario
            if (user.getUsuario() != null && !user.getUsuario().equals(userReq.getUsuario())) {
                List<User> userExist = userRepository.findByUsuarioOverrided(userReq.getUsuario());
                if (!userExist.isEmpty()) {
                    throw new ResourceNotFoundException("El usuario seleccionado ya está en uso");
                }
            }

            // Actualizar datos del usuario
            user.setFirstname(userReq.getFirstname());
            user.setLastname(userReq.getLastname());
            user.setEmail(userReq.getEmail());
            user.setDocType(userReq.getDocType());
            user.setDocNumber(userReq.getDocNumber());
            user.setPhone(userReq.getPhone());
            user.setIsActive(userReq.getIsActive());
            user.setTestMode(userReq.getTestMode());
            user.setIdCompany(userReq.getIdCompany());
            user.setIdBranch(userReq.getIdBranch());
            user.setIdPosition(userReq.getIdPosition());

            // Actualizar roles
            Set<Rol> roles = userReq.getRolIds() != null && !userReq.getRolIds().isEmpty()
                    ? new HashSet<>(rolRepository.findAllById(userReq.getRolIds()))
                    : null;

            if (roles != null) {
                user.setRoles(roles);
            } else {
                user.getRoles().clear();
            }

            // Guardar el usuario actualizado en la base de datos
            return userRepository.save(user);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        } catch (Exception exc) {
            throw new Exception("Error desconocido: " + exc.getMessage());
        }
    }

    public User updateUserStatus(Long id, User userReq) {
        User user = getUser(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró usuario con ID " + id));

        user.setIsActive(userReq.getIsActive());
        user.setTestMode(userReq.getTestMode());

        return userRepository.save(user);
    }

    public User deleteUser(Long id) {
        User user = getUser(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró usuario con ID " + id));

        user.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));

        return userRepository.save(user);
    }
}
