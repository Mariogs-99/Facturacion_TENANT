package sv.gov.cnr.factelectrcnrservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.models.dto.DatosReporteDTO;
import sv.gov.cnr.factelectrcnrservice.models.dto.EnvioDteDTO;
import sv.gov.cnr.factelectrcnrservice.models.dto.ReporteIvaContribuyentesDTO;
import sv.gov.cnr.factelectrcnrservice.services.ReporteService;
import sv.gov.cnr.factelectrcnrservice.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/v1/reporte")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping(path = "/iva")
    public ResponseEntity<Object> getReport(@RequestBody DatosReporteDTO datosReporte) {
        try {
            ByteArrayOutputStream baos = reporteService.generarReporteComplementario(datosReporte);
            byte[] data = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            String filename = datosReporte.getEsCSV() ? "report.csv" : "report.pdf";
            headers.setContentDispositionFormData("attachment", filename);

            MediaType mediaType = datosReporte.getEsCSV() ? new MediaType("text", "csv") : MediaType.APPLICATION_PDF;
            headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);

        }catch (Exception e){
            return Utils.jsonResponse(500, "Error: " + e.getMessage(), null);
        }
    }



}
