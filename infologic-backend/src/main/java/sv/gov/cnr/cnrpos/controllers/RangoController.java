
package sv.gov.cnr.cnrpos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.cnrpos.entities.Rango;
import sv.gov.cnr.cnrpos.entities.Sucursal;
import sv.gov.cnr.cnrpos.repositories.RangoRepository;
import sv.gov.cnr.cnrpos.repositories.SucursalRepository;
import sv.gov.cnr.cnrpos.services.CompanyService;
import sv.gov.cnr.cnrpos.services.RangoService;
import sv.gov.cnr.cnrpos.services.RcatalogoService;
import sv.gov.cnr.cnrpos.services.SucursalService;
import sv.gov.cnr.cnrpos.utils.Utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/v1/rango")
public class RangoController {

    private final SucursalService sucursalService;
    private final CompanyService companyService;
    private final RcatalogoService rcatalogoService;
    private final RangoService rangoService;
    private final SucursalRepository sucursalRepository;
    private final RangoRepository rangoRepository;
    private final Utils utils;

    public RangoController(
            SucursalService sucursalService,
            CompanyService companyService,
            RcatalogoService rcatalogoService,
            RangoService rangoService,
            SucursalRepository sucursalRepository,
            RangoRepository rangoRepository,
            Utils utils
    ) {
        this.sucursalService = sucursalService;
        this.companyService = companyService;
        this.rcatalogoService = rcatalogoService;
        this.rangoService = rangoService;
        this.sucursalRepository = sucursalRepository;
        this.rangoRepository = rangoRepository;
        this.utils = utils;
    }

    @PostMapping()
    public Object registerNewRango(@RequestBody Rango rango){
        try {
            return utils.jsonResponse(200, "Rango Agregado con éxito", rangoService.saveRango(rango));


        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo Actualizar el Rango", ex.getMessage());

        }

    }



    @DeleteMapping("/{id}")
    public Object deleteRango(@PathVariable Long id) {
        System.out.println("id = " + id);

        try {
            Rango rango = rangoService.getRango(id);

            System.out.println("rango = " + rango);

            if (rango == null) {
                return ResponseEntity.notFound().build();
            }

            rango.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
            rangoService.saveRango(rango);

            return utils.jsonResponse(200, "Se elimino correctamente", null);
        } catch (Exception ex) {
            return utils.jsonResponse(500, "No se pudo eliminar", ex.getMessage());

        }
    }


    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok().build();
    }


}
