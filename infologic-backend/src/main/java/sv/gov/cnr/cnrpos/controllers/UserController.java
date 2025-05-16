package sv.gov.cnr.cnrpos.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.entities.Company;
import sv.gov.cnr.cnrpos.entities.Sucursal;
import sv.gov.cnr.cnrpos.exceptions.ResourceNotFoundException;
import sv.gov.cnr.cnrpos.models.dto.UserDTO;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.repositories.security.UserRepository;
import sv.gov.cnr.cnrpos.security.AuthenticationRequest;
import sv.gov.cnr.cnrpos.security.AuthenticationService;
import sv.gov.cnr.cnrpos.services.*;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.util.*;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final CompanyService companyService;
    private final SucursalService sucursalService;
    private final Utils utils;
    private final AuthenticationService authenticationService;

    @Value("${configuracion.password-user-new}")
    private String pass;

    @GetMapping("/index")
    public ResponseEntity<?> index() {

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Object> getPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size, @RequestParam(defaultValue = "", name = "sortBy") String sortBy, @RequestParam(required = false, defaultValue = "", name = "filterBy") String filterBy) {
        Map<String, Object> response = new HashMap<>();




        response.put("pages", userService.getPage(page, size, filterBy, sortBy));



        return utils.jsonResponse(200, "usuarios", response);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getList() {
        try {

            Map<String, Object> response = new HashMap<>();
            response.put("docTypes", categoryService.getCategoryDto("TIPO_DOCUMENTO_IDENTIFICACION"));
            response.put("cargoAdmin", categoryService.getCategoryDto("CARGO_ADMIN"));
            response.put("company", companyService.getAllCompanies());
            List<Sucursal> sucursals = sucursalService.getAllSucursales();
            sucursals.stream().forEach(sucursal -> sucursal.setNombre(sucursal.getNombre() + " " + sucursal.getMisional()));
            response.put("branch", sucursals);

            return utils.jsonResponse(200, "listado de informacion", response);

        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo listar", ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id, @RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        try {
            UserDTO userDto = userService.getUserDTO(id);

            //Se obtiene dui y nit
            List<Long> list = Arrays.asList(1529L, 1530L);

            Map<String, Object> response = new HashMap<>();
            response.put("user", userDto);
            response.put("docTypes", categoryService.getCategoryDtoIn(list));
            response.put("cargoAdmin", categoryService.getCategoryDto("CARGO_ADMIN"));

            if (userDto.getIdCompany() != null) {
                Company company = companyService.getCompany(userDto.getIdCompany());
                response.put("company", company != null ? company : null);
                List<Sucursal> sucursals = company != null
                        ? sucursalService.getSucursalByCompanyId(company.getIdCompany())
                        : Collections.emptyList();
                response.put("branch", sucursals);
            }

            return utils.jsonResponse(200, "usuario con id " + id, response);

        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo obtener el usuario", ex.getMessage());
        }
    }

    @PostMapping("/byusername")
    public ResponseEntity<Object> getUserByUsername(@RequestBody User usuario, @RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        try {
            UserDTO userDto = userService.getByUsername(usuario.getUsuario());

            //Se obtiene dui y nit
            List<Long> list = Arrays.asList(1529L, 1530L);

            Map<String, Object> response = new HashMap<>();
            response.put("user", userDto);
            response.put("docTypes", categoryService.getCategoryDtoIn(list));
            response.put("cargoAdmin", categoryService.getCategoryDto("CARGO_ADMIN"));

            if (userDto.getIdCompany() != null) {
                Company company = companyService.getCompany(userDto.getIdCompany());
                response.put("company", company != null ? company : null);
                List<Sucursal> sucursals = company != null
                        ? sucursalService.getSucursalByCompanyId(company.getIdCompany())
                        : Collections.emptyList();
                response.put("branch", sucursals);
            }

            return utils.jsonResponse(200, "usuario con usuario: " + usuario.getUsuario(), response);

        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo obtener el usuario", ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody User user) {
        try {

            return utils.jsonResponse(200, "Se agregó el usuario exitosamente", userService.saveUser(user));
        } catch (ResourceNotFoundException e) {
            return utils.jsonResponse(400, "Error: " + e.getMessage(), null);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se agregó el usuario", ex.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            return utils.jsonResponse(200, "se actualizo el usuario exitosamente", userService.updateUser(id, user));

        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se actualizo el usuario", ex.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Object> updateUserStatus(@PathVariable Long id, @RequestBody User user) {
        try {
            return utils.jsonResponse(200, "se actualizo el usuario exitosamente", userService.updateUserStatus(id, user));

        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se actualizo el usuario", null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            return utils.jsonResponse(200, "se elimino el usuario correctamente", userService.deleteUser(id));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo eliminar", ex.getMessage());
        }
    }



}
