package sv.gov.cnr.cnrpos.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.models.security.Rol;
import sv.gov.cnr.cnrpos.services.PermissionService;
import sv.gov.cnr.cnrpos.services.RolService;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RolService rolService;
    private final PermissionService permissionService;
    private final Utils utils;


    @GetMapping("/index")
    public ResponseEntity<Object> index() {
        return utils.jsonResponse(200, "usuarios", rolService.getAllRoles());
    }

    @GetMapping("/page")
    public ResponseEntity<Object> getPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("pages", rolService.getPage(page, size));
            return utils.jsonResponse(200, "usuarios", response);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo listar", ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRole(@PathVariable Long id, @RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("rol", rolService.getRolDto(id));
            response.put("permisos", permissionService.getPage(page, size));
            return utils.jsonResponse(200, "usuario", response);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "Rol no se pudo obtener", ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> registerNewRole(@RequestBody Rol role) {
        try {
            return utils.jsonResponse(200, "role agregado", rolService.saveRol(role));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "Rol no se pudo agregar", ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable Long id, @RequestBody Rol rolDetails) {
        try {
            return utils.jsonResponse(200, "actualizacion de rol", rolService.updateRol(id, rolDetails));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo actualizar el rol", ex.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRol(@PathVariable Long id) {
        try {
            Rol rol = rolService.getRol(id);

            if (rol == null) {
                return ResponseEntity.notFound().build();
            }

            rol.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            rolService.saveRol(rol);

            return utils.jsonResponse(200, "Se elimino correctamente", null);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo eliminar", ex.getMessage());
        }
    }
}
