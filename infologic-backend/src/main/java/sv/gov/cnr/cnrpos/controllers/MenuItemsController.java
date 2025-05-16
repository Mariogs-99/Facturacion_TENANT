package sv.gov.cnr.cnrpos.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.services.MenuItemsService;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.util.HashMap;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/menu-items")
@RequiredArgsConstructor
public class MenuItemsController {

    private final MenuItemsService menuItemsService;
    private final Utils utils;

    @Autowired


//    @GetMapping("/index")
//    public ResponseEntity<Object> index() {
//        try {
//            return utils.jsonResponse(200, "menu items", menuItemsService.getMenuItems());
//        } catch (Exception ex) {
//            return utils.jsonResponse(500, "faltal error", ex.getMessage());
//
//        }
//
//    }

    @GetMapping("/index")
    public ResponseEntity<Object> index() {
        try {
            String tenant = sv.gov.cnr.cnrpos.config.TenantContext.getCurrentTenant();
            if (tenant == null) {
                return utils.jsonResponse(400, "Tenant no configurado aún", null);
            }
            return utils.jsonResponse(200, "menu items", menuItemsService.getMenuItems());
        } catch (Exception ex) {
            return utils.jsonResponse(500, "fatal error", ex.getMessage());
        }
    }


    @GetMapping(path = "/{id}/page")
    public ResponseEntity<Object> getPage(@PathVariable Long id, @RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "5", name = "size") int size) {
        Map<String, Object> response = new HashMap<>();

        response.put("pages", menuItemsService.getPage(id, page, size));

        return utils.jsonResponse(200, "listado menu", response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemMenu(@PathVariable Long id) {
        try {
            return utils.jsonResponse(200, "menu items", menuItemsService.getMenuItemDto(id));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "faltal error", ex.getMessage());

        }
    }

    @PostMapping
    public ResponseEntity<Object> saveMenuItem(@RequestBody MenuItems menuItem) {
        try {
            return utils.jsonResponse(200, "menu items", menuItemsService.saveMenuItem(menuItem));

        } catch (Exception ex) {
            return utils.jsonResponse(500, "faltal error", ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMenuItem(@PathVariable Long id, @RequestBody MenuItems menuItem) {
        try {
            return utils.jsonResponse(200, "Item menu editado correctamente", menuItemsService.updateMenuItems(id, menuItem));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se puedo editar", ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMenuItems(@PathVariable Long id) {
        try {
            return utils.jsonResponse(200, "se elimino el item de menu correctamente", menuItemsService.deleteMenuItem(id));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo eliminar", ex.getMessage());
        }
    }


}
