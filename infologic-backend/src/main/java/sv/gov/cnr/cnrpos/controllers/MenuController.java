package sv.gov.cnr.cnrpos.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.security.AuthenticationService;
import sv.gov.cnr.cnrpos.services.CategoryService;
import sv.gov.cnr.cnrpos.services.MenuService;
import sv.gov.cnr.cnrpos.utils.Utils;

import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/v1/menu")
@AllArgsConstructor
public class MenuController {


    private final Utils utils;
    private final MenuService menuService;
    private final AuthenticationService authenticationService;
    private final CategoryService categoryService;

    @GetMapping(path = "/index")
    public ResponseEntity<Object> index() {
        return utils.jsonResponse(200, "listado menu", menuService.getAllMenu());
    }

    @GetMapping(path = "/page")
    public ResponseEntity<Object> getPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        Map<String, Object> response = new HashMap<>();

        response.put("pages", menuService.getPage(page, size));

        if (page == 0) {
            response.put("categories", categoryService.getCategoryDto("TIPO_MENU"));
        }

        return utils.jsonResponse(200, "listado menu", response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getMenu(@PathVariable Long id) {
        return utils.jsonResponse(200, "menu", menuService.getMenuMenuItems(id));
    }

    @PostMapping
    public ResponseEntity<Object> saveMenu(@RequestBody Menu menu) {
        try {
            return utils.jsonResponse(200, "se guardo el menu correctamente", menuService.saveMenu(menu));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo guardar el menu", ex.getMessage());
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> updateMenu(@PathVariable Long id, @RequestBody Menu menu) {
        try {
            return utils.jsonResponse(200, "el menu se edito correctamente", menuService.updateMenu(id, menu));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo editar el menu", null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMenu(@PathVariable Long id) {
        try {
            return utils.jsonResponse(200, "se elimino el menu correctamente", menuService.deleteMenu(id));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo eliminar el menu", ex.getMessage());
        }
    }
}
