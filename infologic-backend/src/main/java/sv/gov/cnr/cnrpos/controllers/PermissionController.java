package sv.gov.cnr.cnrpos.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.services.MenuService;
import sv.gov.cnr.cnrpos.services.PermissionService;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.util.HashMap;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/v1/permissions")
@AllArgsConstructor
public class PermissionController {

    private final Utils utils;
    private final PermissionService permissionService;
    private final MenuService menuService;

    @GetMapping(path = "/index")
    public ResponseEntity<Object> index() {
        return utils.jsonResponse(200, "index permisos", permissionService.getAllPermissions());
    }

    @GetMapping(path = "/page")
    public ResponseEntity<Object> getPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("pages", permissionService.getPage(page, size));
            return utils.jsonResponse(200, "listado permisos", response);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se encontro listado de permisos", ex.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getPermission(@PathVariable Long id) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("permiso", permissionService.getPermission(id));
            response.put("menu", menuService.getAllMenuDTO());
            return utils.jsonResponse(200, "permiso", response);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se encontro permiso", ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> registerNewPermission(@RequestBody Permission permission) {
        try {
            return utils.jsonResponse(200, "permiso agregado", permissionService.savePermission(permission));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se guardo el permiso", ex.getMessage());
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> updatePermission(@PathVariable Long id, @RequestBody Permission permissionReq) {
        try {
            return utils.jsonResponse(200, "actualizacion de usuarios", permissionService.updatePermission(id, permissionReq));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se actualizo el permiso", ex.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deletePermission(@PathVariable Long id) {
        try {
            return utils.jsonResponse(200, "permiso eliminado", permissionService.deletePermission(id));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se elimino el permiso", ex.getMessage());
        }
    }
}
