package sv.gov.cnr.factelectrcnrservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.factelectrcnrservice.exceptions.DTEException;
import sv.gov.cnr.factelectrcnrservice.models.dto.EspecificoCP;
import sv.gov.cnr.factelectrcnrservice.services.ComprobantePagoService;

@RestController
@RequestMapping(path = "/api/v1/dte/consulta")
public class ComprobanteController {

    private final ComprobantePagoService comprobantePagoService;

    @Autowired
    public ComprobanteController(ComprobantePagoService comprobantePagoService) {
        this.comprobantePagoService = comprobantePagoService;
    }

    @GetMapping("/valcp/{numeroComprobante}")
    public EspecificoCP consultaComprobante(@PathVariable String numeroComprobante) throws DTEException {
        return comprobantePagoService.cambioEspecificoCP(numeroComprobante);
    }
}
