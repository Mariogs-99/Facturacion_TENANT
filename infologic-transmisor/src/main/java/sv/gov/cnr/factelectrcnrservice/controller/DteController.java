package sv.gov.cnr.factelectrcnrservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gov.cnr.factelectrcnrservice.entities.CnrposConfWhatsapp;
import sv.gov.cnr.factelectrcnrservice.entities.CnrposUploadFile;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.exceptions.DTEException;
import sv.gov.cnr.factelectrcnrservice.models.dto.*;
import sv.gov.cnr.factelectrcnrservice.services.*;
import sv.gov.cnr.factelectrcnrservice.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/api/v1/dte")
@RequiredArgsConstructor
public class DteController {


    private final ReporteService reporteService;
    private final TransaccionService transaccionService;
    private final DTEService dteService;
    private final EmailService emailService;
    private final WhatsappService whatsappService;
    private final CnrposConfWhatsappService confWhatsappService;
    private final DteTransaccionService dteTransaccionService;
    private final CnrposUploadFileService cnrposUploadFileService;

    private final ComprobantePagoService comprobantePagoService;


    @GetMapping(path = "/reporte/pdf/{codigoGeneracion}")
    public ResponseEntity<String> getReportBase64(@PathVariable String codigoGeneracion) {
        try {
            Transaccion transaccion = transaccionService.findByCodigoGeneracion(codigoGeneracion);

            ByteArrayOutputStream reportStream = reporteService.generateReportToStream(transaccion);

            // Convertir el ByteArrayOutputStream a una cadena base64
            String base64Report = Base64.getEncoder().encodeToString(reportStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+codigoGeneracion+".pdf");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            // Crear una respuesta con el PDF en base64
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON) // Ajuste el tipo de contenido a JSON
                    .body(base64Report);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: ".concat(e.getMessage()));
        }
    }

    @GetMapping(path = "/reporte/{codigoGeneracion}")
    public ResponseEntity<Object> getReport(@PathVariable String codigoGeneracion) {
        try {
            Transaccion transaccion = transaccionService.findByCodigoGeneracion(codigoGeneracion);

            ByteArrayOutputStream reportStream = reporteService.generateReportToStream(transaccion);

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(reportStream.toByteArray()));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+codigoGeneracion+".pdf");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        }catch (Exception e){
            return Utils.jsonResponse(500, "Error: " + e.getMessage(), null);
        }
    }

    @PostMapping(path = "/anularDte/{codigoGeneracion}")
    public ResponseEntity<Object> anularDte(@PathVariable String codigoGeneracion, @RequestBody MotivoAnulacionDTO motivoAnulacion) {
        try {
            Transaccion transaccion = transaccionService.findByCodigoGeneracion(codigoGeneracion);
            Map<String, Object> data = dteService.invalidarDte(transaccion, motivoAnulacion);
            if(data.get("estado").equals("PROCESADO")){
                dteService.enviarDteAnulado(transaccion);
                return Utils.jsonResponse(200, "Petici n realizada exitosamente", data);
            }else {
                return Utils.jsonResponse(400, "Error al invalidar DTE", data);
            }
        }catch (Exception e){
            return Utils.jsonResponse(500, "Error: " + e.getMessage(), null);
        }
    }


    //@CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(path = "/enviar")
    public ResponseEntity<Object> enviarDte(@RequestBody EnvioDteDTO datosEnvio){
        try{
            Transaccion transaccion = transaccionService.findByCodigoGeneracion(datosEnvio.getCodigoGeneracion());
            ByteArrayOutputStream reportStream = reporteService.generateReportToStream(transaccion);
            Boolean correoEnviado = emailService.sendDteViaEmail(transaccion, reportStream, datosEnvio.getCorreo());
            if (correoEnviado.equals(Boolean.TRUE)){
                return Utils.jsonResponse(200, "Petición realizada exitosamente", null);
            }

            return Utils.jsonResponse(400, "No fue posible realizar el envio del correo electronico", null);
        }catch (Exception e){
            return Utils.jsonResponse(500, "Error: " + e.getMessage(), null);
        }

    }

    @PostMapping(path = "/enviarVarios")
    public ResponseEntity<Object> enviarDteVarios(@RequestBody EnvioDteVariosDTO datosEnvio) {

        if (datosEnvio.getCorreo().length == 0){
            return Utils.jsonResponse(400, "Favor ingresar un email", null);
        }

        try {
            Transaccion transaccion = transaccionService.findByCodigoGeneracion(datosEnvio.getCodigoGeneracion());

            try (ByteArrayOutputStream reportStream = reporteService.generateReportToStream(transaccion)) {
                for (String correo : datosEnvio.getCorreo()) {
                        emailService.sendDteViaEmail(transaccion, reportStream, correo);
                }

                return Utils.jsonResponse(200, "Petici n realizada exitosamente", null);

            }
            // Log error al cerrar el stream (opcional)

        } catch (Exception e) {
            return Utils.jsonResponse(500, "Error: " + e.getMessage(), null);
        }
    }

    @GetMapping(value = "/send-message/{codigoGeneracion}/{numero}", produces = "application/json")
    public ResponseEntity<Object> sendMessage(@PathVariable String codigoGeneracion, @PathVariable String numero) {
        CnrposConfWhatsapp cnrposConfWhatsapp = confWhatsappService.findLastActive().orElse(null);
        String url = Objects.requireNonNull(cnrposConfWhatsapp).getUrlEndpoint().replace("$_ver", cnrposConfWhatsapp.getVersionApp()).replace("$_id", cnrposConfWhatsapp.getIdNumero());
        WhatsAppMessageRequest request = buildMessageRequest(cnrposConfWhatsapp, codigoGeneracion, numero);

        WhatsAppMessageResponse whatsAppMessageResponse = whatsappService.sendMessage(request, url, cnrposConfWhatsapp.getToken());
        if (whatsAppMessageResponse.getError() == null){
            return Utils.jsonResponse(200, "Mensaje enviado exitosamente", null);
        }else{
            if (whatsAppMessageResponse.getError().getCode() == HttpStatus.UNAUTHORIZED.value()){
                return Utils.jsonResponse(400, "Ocurrio un error al enviar mensaje, token vencido", null);
            }else{
                return Utils.jsonResponse(400, "Ocurrio un error al enviar mensaje ", whatsAppMessageResponse.getError().getMessage());
            }
        }
    }

    private WhatsAppMessageRequest buildMessageRequest(CnrposConfWhatsapp cnrposConfWhatsapp, String codigoGeneracion, String numero) {
        CnrposUploadFile cnrposUploadFile = cnrposUploadFileService.getActivo().orElse(null);
        Transaccion transaccion = transaccionService.findByCodigoGeneracion(codigoGeneracion);
        var infoDte = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        WhatsAppMessageRequest.Language language = new WhatsAppMessageRequest.Language();
        language.setCode("es_MX");

        WhatsAppMessageRequest.Parameter parameter = new WhatsAppMessageRequest.Parameter();
        parameter.setType("text");
        assert cnrposUploadFile != null;
        parameter.setText(cnrposUploadFile.getUrlConsulta().replace("$_ID", infoDte.getIdPdf()));

        WhatsAppMessageRequest.Component component = new WhatsAppMessageRequest.Component();
        component.setType("body");
        component.setParameters(Collections.singletonList(parameter));

        WhatsAppMessageRequest.Template template = new WhatsAppMessageRequest.Template();
        template.setName(cnrposConfWhatsapp.getNombreTemplate());
        template.setLanguage(language);
        template.setComponents(Collections.singletonList(component));

        WhatsAppMessageRequest request = new WhatsAppMessageRequest();
        request.setMessaging_product("whatsapp");
        request.setTo(numero);
        request.setType("template");
        request.setTemplate(template);

        return request;
    }

    @GetMapping("/especificoCP")
    public EspecificoCP especificoCP(@RequestParam String comprobantePago) throws DTEException {
        return comprobantePagoService.cambioEspecificoCP(comprobantePago);
    }


}

