package sv.gov.cnr.cnrpos.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.entities.Item;
import sv.gov.cnr.cnrpos.services.ItemService;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true") // EMTORNO DEV
//@CrossOrigin(origins = "https://tudominio.com", allowedHeaders = "*", allowCredentials = "true") //ENTORNO PROD
@RequestMapping(path = "/api/v1/item")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(path = "dte/{codigoGeneracion}")
    public ResponseEntity<Object> getPermission(@PathVariable("codigoGeneracion") String codigoGeneracion) {
        try {
            List<Item> items = itemService.itemsByDte(codigoGeneracion);
            return Utils.jsonResponse(200, "Petición realizada exitosamente", items);
        }catch (Exception ex){
            return Utils.jsonResponse(500, "Error: " +ex.getMessage(), null);
        }
    }

    @PostMapping(path = "/itemsByCodes")
    public ResponseEntity<Object> getItemsByTransactionCodes(@RequestBody List<String> codes) {
        try {
            List<Item> items = (List<Item>) itemService.itemsByMultipleDtes(codes);
            if (items.isEmpty()) {
                return Utils.jsonResponse(404, "No se encontraron ítems para los códigos proporcionados", null);
            }
            return Utils.jsonResponse(200, "Ítems recuperados exitosamente", items);
        } catch (Exception ex) {
            return Utils.jsonResponse(500, "Error interno del servidor: " + ex.getMessage(), null);
        }
    }


}
