package sv.gov.cnr.cnrpos.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.entities.Compra;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.services.CategoryService;
import sv.gov.cnr.cnrpos.services.CompraService;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/compras")
@AllArgsConstructor
public class ComprasController {

    private final Utils utils;
    private final CompraService compraService;
    private final CategoryService categoryService;

    @GetMapping(path = "/page")
    public ResponseEntity<Object> getPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        try {
            Map<String, Object> response = new HashMap<>();

            // ids de catalogo
            List<Long> list = Arrays.asList(35L, 36L, 38L, 39L);


            response.put("pages", compraService.getPage(page, size));
            response.put("operacion_tipo", categoryService.getCategoryDto("CONDICION_OPERACION"));
            response.put("documento_tipo", categoryService.getCategoryDtoIn(list));


            return utils.jsonResponse(200, "listado compras", response);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se encontro listado de compras", ex.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getCompra(@PathVariable Long id) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("compras", compraService.getCompraDTO(id));

            return utils.jsonResponse(200, "compras", response);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se encontro el registro", ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> registerNewCompra(@RequestBody Compra compra) {
        try {
            return utils.jsonResponse(200, "registro agregado", compraService.saveCompra(compra));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se guardo el registro", ex.getMessage());
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> updateCompra(@PathVariable Long id, @RequestBody Compra compraReq) {
        try {
            return utils.jsonResponse(200, "actualizacion de compras", compraService.updateCompra(id, compraReq));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se actualizo el registro", ex.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deletePermission(@PathVariable Long id) {
        try {
            return utils.jsonResponse(200, "registro eliminado", compraService.deleteCompra(id));
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se elimino el registro", ex.getMessage());
        }
    }
}
