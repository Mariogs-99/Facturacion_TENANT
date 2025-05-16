package sv.gov.cnr.factelectrcnrservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sv.gov.cnr.factelectrcnrservice.config.AppConfig;
import sv.gov.cnr.factelectrcnrservice.entities.Company;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.exceptions.DTEException;
import sv.gov.cnr.factelectrcnrservice.models.dto.*;
import sv.gov.cnr.factelectrcnrservice.models.enums.EstatusCola;
import sv.gov.cnr.factelectrcnrservice.models.enums.VersionTipoDTE;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HaciendaApiService {

    @Value("${configuracion.ambiente}")
    String ambiente;
    @Value("${configuracion.api-hacienda.endpoint-auth}")
    private String autenticacionMHEndpoint;
    @Value("${configuracion.api-firmador.endpoint-firmar}")
    private String firmaDigitalEndpoint;
    //URL DESARROLLO/PRUEBAS MH
    @Value("${configuracion.api-hacienda.endpoint-transmitir-dte-individual}")
    private String transmitirDteIndividualMHEndpoint;
    @Value("${configuracion.api-hacienda.endpoint-anular-dte-individual}")
    private String anularDteIndividualMHEndpoint;
    @Value("${configuracion.api-hacienda.endpoint-consultar-dte-individual}")
    private String consultarDteIndividualMHEndpoint;
    @Value("${configuracion.api-hacienda.endpoint-evento-contingencia}")
    private String contingenciaDteIndividualMHEndpoint;
    //URL PRODUCCION MH
    @Value("${configuracion.api-hacienda.endpoint-auth-pro}")
    private String autenticacionMHEndpointPro;
    @Value("${configuracion.api-hacienda.endpoint-transmitir-dte-individual-pro}")
    private String transmitirDteIndividualMHEndpointPro;
    @Value("${configuracion.api-hacienda.endpoint-anular-dte-individual-pro}")
    private String anularDteIndividualMHEndpointPro;
    @Value("${configuracion.api-hacienda.endpoint-consultar-dte-individual-pro}")
    private String consultarDteIndividualMHEndpointPro;
    @Value("${configuracion.api-hacienda.endpoint-evento-contingencia-pro}")
    private String contingenciaDteIndividualMHEndpointPro;

// URL COMPROBANTE DE PAGOS

//@Value("${configuracion.api-cnr.endpoint-consultar-comprobante-pago}")
private String consultaComprobantePago;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CompanyService companyService;
    private final TokenService tokenService;
    private final AppConfig appConfig;
    private final DteTransaccionService dteTransaccionService;
    private final TransaccionService transaccionService;


    public String autenticar() throws DTEException {
        //verificar si existe un token guardado recientemente en la BD
        var token = tokenService.obtenerTokenPorHorasTranscurridas(appConfig.getHorasToken()).orElse(null);
        if (token != null) {
            return token.getToken();
        }
        var company = companyService.getEmisor();
        //de lo contrario, volverse a autenticar para crearlo
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String requestBody = "user=%s&pwd=%s".formatted(company.getNit(), company.getMhPass());
        var requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = null;
        if (ambiente.equalsIgnoreCase("00")){
            responseEntity = restTemplate.postForEntity(autenticacionMHEndpoint, requestEntity, String.class);
            log.info("*****************************pruebas autenticar**************************************************+");
            log.info(ambiente);
        }else{
            responseEntity = restTemplate.postForEntity(autenticacionMHEndpointPro, requestEntity, String.class);
        }
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Map<String, Object> responseMap = null;
            try {
                responseMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                throw new DTEException("No se pudo leer el cuerpo de la respuesta al autenticar.");
            }
            var status = responseMap.get("status").toString();
            var body = (Map<String, Object>) responseMap.get("body");
            if ("OK".equalsIgnoreCase(status)) {
                var nuevoToken = body.get("token").toString();
                tokenService.guardarToken(nuevoToken);
                return nuevoToken;
            } else {
                var codigoError = body.get("codigoMsg").toString();
                var mensajeError = body.get("descripcionMsg").toString();
                throw new DTEException("Error en la autenticación. Código Respuesta: [%s]. Mensaje Respuesta: [%s]".formatted(codigoError, mensajeError));
            }
        } else {
            // Manejar otros códigos de respuesta HTTP
            throw new DTEException("Error en la autenticación. Status HTTP: " + responseEntity.getStatusCode());
        }
    }

    public String firmarDocumento(Object dte) throws DTEException, JsonProcessingException {
        if (dte == null) {
            throw new DTEException("EL JSON DTE a firmar no puede estar vacío");
        }
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var company = companyService.getEmisor();
        String payload = objectMapper.writeValueAsString(new MhFirmadorRequest(
                company.getNit(),
                company.getClavePrimariaCert(),
                true,
                dte));

        var requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(firmaDigitalEndpoint, requestEntity, String.class);
        } catch (Exception e) {
            throw new DTEException("El servicio firmador no está disponible");
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new DTEException("Error en la comunicación con el servicio firmador. HTTP Status: %s".formatted(responseEntity.getStatusCode()));
        }

        try {
            Map<String, Object> responseBodyMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {
            });
            log.info(responseBodyMap.toString());
            if (!"ok".equalsIgnoreCase(responseBodyMap.get("status").toString())) {
                var infoError = responseBodyMap.get("body");
                throw new DTEException("Error al firmar el documento. Error: %s".formatted(infoError.toString()));
            }
            return responseBodyMap.get("body").toString();
        } catch (Exception e) {
            throw new DTEException("Error al firmar el documento. No se pudo leer la respuesta.");
        }
    }

    /*
    * //Manual Técnico para la Integración Tecnológica del Sistema de Transmisión, pagina 16:
    *1. Si al momento de enviar un DTE, el servicio de Documentos Tributarios Electrónicos no
    responde después de 8 segundos.
    a. Enviar consulta de estado sobre el documento transmitido, para verificar si no ha
    sido recibido.
    b. Si no ha sido recibido, enviar una nueva solicitud para recepción. Repetir hasta
    obtener la respuesta exitosa, un máximo de 2 veces.
    2. Si al momento de enviar un DTE, el servicio del emisor falla y no procesa la respuesta del
    servicio de recepción.
    a. Enviar consulta de estado sobre el documento transmitido, para verificar si no ha
    sido recibido.
    b. Si no ha sido recibido, enviar una nueva solicitud para recepción. Repetir hasta
    obtener la respuesta exitosa, un máximo de 2 veces.
    * */
    public String enviarDTE(Transaccion transaccion) throws DTEException {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String payload;
        String estado = null;
        DteTransaccion infoDte = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        try {
            headers.set(HttpHeaders.AUTHORIZATION, autenticar());
            payload = objectMapper.writeValueAsString(new MhRecepcionDte(
                    appConfig.getAmbiente(),
                    transaccion.getIdTransaccion().intValue(),
                    VersionTipoDTE.obtenerVersionPorCodigo(transaccion.getTipoDTE()),
                    transaccion.getTipoDTE(),
                    infoDte.getDteJsonFirmado(),
                    infoDte.getCodigoGeneracion()
            ));
            var requestEntity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> responseEntity = null;
            if (ambiente.equalsIgnoreCase("00")){
                responseEntity = restTemplate.postForEntity(transmitirDteIndividualMHEndpoint, requestEntity, String.class);
            }else{
                responseEntity = restTemplate.postForEntity(transmitirDteIndividualMHEndpointPro, requestEntity, String.class);
            }
            Map<String, Object> responseBodyMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {
            });
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                infoDte.setEstadoDte(responseBodyMap.get("estado").toString());
                infoDte.setSelloRecibidoMh(responseBodyMap.get("selloRecibido").toString());
                infoDte.setFechaProcesado(responseBodyMap.get("fhProcesamiento").toString());
                infoDte.setVersionApp(responseBodyMap.get("versionApp").toString());
                infoDte.setObservaciones(responseBodyMap.get("observaciones").toString());
                estado = responseBodyMap.get("estado").toString();
            }
        } catch (HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            JSONObject json = new JSONObject(body);
            estado = json.optString("estado");
            infoDte.setEstadoDte(estado);
            infoDte.setObservaciones(body);
        } catch (JsonProcessingException e) {
            throw new DTEException("No se pudo leer el cuerpo de la respuesta al enviar el DTE");
        } finally {
            dteTransaccionService.save(infoDte);
        }
        return estado;
    }


    /**
     * @param transaccion a consultar para validar si fue enviado o no a MH API
     * @return sello de recibido en caso de haber sido enviado
     * @throws DTEException errores en consulta de DTE.
     */
    public String consultarDte(Transaccion transaccion) throws DTEException {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String payload;
        String estadoDte = null;
        DteTransaccion infoDte = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        Company company = companyService.getEmisor();
        try {
            headers.set(HttpHeaders.AUTHORIZATION, autenticar());
            payload = objectMapper.writeValueAsString(
                    new MhConsultaDte(
                            company.getNit(),
                            infoDte.getTipoDTE(),
                            infoDte.getCodigoGeneracion()
                    )
            );
            var requestEntity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> responseEntity = null;
            if (ambiente.equalsIgnoreCase("00")){
                responseEntity = restTemplate.postForEntity(consultarDteIndividualMHEndpoint, requestEntity, String.class);
            }else{
                responseEntity = restTemplate.postForEntity(consultarDteIndividualMHEndpointPro, requestEntity, String.class);
            }
            Map<String, Object> responseBodyMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {
            });
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                infoDte.setEstadoDte(responseBodyMap.get("estado").toString());
                infoDte.setSelloRecibidoMh(responseBodyMap.get("selloRecibido").toString());
                infoDte.setFechaProcesado(responseBodyMap.get("fhProcesamiento").toString());
                infoDte.setVersionApp(responseBodyMap.get("versionApp").toString());
                estadoDte = responseBodyMap.get("estado").toString();
                infoDte.setObservaciones(responseBodyMap.get("observaciones").toString());
                dteTransaccionService.save(infoDte);
            }
            if(responseEntity.getStatusCode().equals(HttpStatus.ACCEPTED)){
                infoDte.setEstadoDte(responseBodyMap.get("estado").toString());
                infoDte.setObservaciones(responseBodyMap.get("observaciones").toString());
                estadoDte = responseBodyMap.get("estado").toString();
            }
        }catch(HttpClientErrorException e){
            return estadoDte;
        } catch (JsonProcessingException e) {
            throw new DTEException("No fue posible leer la respuesta");
        }
        return estadoDte;
    }

    public String notificarContingencia(Transaccion transaccion) throws DTEException {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String payload;
        DteTransaccion infoDte = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        Company emisor = companyService.getEmisor();
        String estado = null;
        try {
            headers.set(HttpHeaders.AUTHORIZATION, autenticar());
            payload = objectMapper.writeValueAsString(
                    new MhContingenciaDte(
                            emisor.getNit(),
                            infoDte.getDteJsonFirmado()
                    )
            );
            var requestEntity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> responseEntity = null;
            if (ambiente.equalsIgnoreCase("00")){
                responseEntity = restTemplate.postForEntity(contingenciaDteIndividualMHEndpoint, requestEntity, String.class);
            }else{
                responseEntity = restTemplate.postForEntity(contingenciaDteIndividualMHEndpointPro, requestEntity, String.class);
            }
            Map<String, Object> responseBodyMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {
            });
            if (responseEntity.getStatusCode().equals(HttpStatus.OK) && responseBodyMap.get("selloRecibido") != null) {
                infoDte.setEstadoDte(responseBodyMap.get("estado").toString());
                infoDte.setSelloRecibidoMhCont(responseBodyMap.get("selloRecibido").toString());
                infoDte.setFechaProcesado(responseBodyMap.get("fechaHora").toString());
            }else{
                infoDte.setEstadoDte(responseBodyMap.get("estado").toString());
                infoDte.setFechaProcesado(responseBodyMap.get("fechaHora").toString());
                infoDte.setObservaciones(responseBodyMap.get("observaciones").toString());
            }
            estado = responseBodyMap.get("estado").toString();
        }catch (HttpClientErrorException e){
            String body = e.getResponseBodyAsString();
            JSONObject json = new JSONObject(body);
            estado = json.optString("estado");
            infoDte.setEstadoDte(estado);
            infoDte.setObservaciones(body);
        } catch (DTEException e) {
            throw new DTEException("Error de autenticación");
        } catch (JsonProcessingException e) {
            throw new DTEException("Error al leer la respuesta");
        } finally {
            dteTransaccionService.save(infoDte);
        }
        return estado;
    }

    public Map<String, Object> invalidarDte(Transaccion transaccion) throws DTEException {
        Map<String, Object> data = new HashMap<>();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String payload;
        DteTransaccion infoDte = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        try {
            headers.set(HttpHeaders.AUTHORIZATION, autenticar());
            payload = objectMapper.writeValueAsString(
                    new MhAnulacionDte(
                            appConfig.getAmbiente(),
                            transaccion.getIdTransaccion().intValue(),
                            infoDte.getVersion(),
                            infoDte.getDteJsonFirmado()
                    )
            );
            var requestEntity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> responseEntity = null;
            if (ambiente.equalsIgnoreCase("00")){
                responseEntity = restTemplate.postForEntity(anularDteIndividualMHEndpoint, requestEntity, String.class);
            }else{
                responseEntity = restTemplate.postForEntity(anularDteIndividualMHEndpointPro, requestEntity, String.class);
            }
            Map<String, Object> responseBodyMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {
            });
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                infoDte.setEstadoDte(EstatusCola.ANULADO.name());
                infoDte.setSelloRecibidoMhAnul(responseBodyMap.get("selloRecibido").toString());
                infoDte.setFechaProcesado(responseBodyMap.get("fhProcesamiento").toString());
                infoDte.setVersionApp(responseBodyMap.get("versionApp").toString());
                transaccion.setStatus(EstatusCola.ANULADO);
                transaccion.setUser_update(infoDte.getTransaccion().getUser());
            } else {
                infoDte.setEstadoDte(responseBodyMap.get("estado").toString());
            }
            data.put("estado", responseBodyMap.get("estado"));
            data.put("observaciones", responseBodyMap.get("observaciones"));
        }catch(HttpClientErrorException e){
            String body = e.getResponseBodyAsString();
            JSONObject json = new JSONObject(body);
            var estado = json.optString("estado");
            data.put("estado", estado);
            data.put("observaciones", json.optJSONArray("observaciones"));

            JSONArray observaciones = json.optJSONArray("observaciones");

            if (observaciones == null){
                observaciones = new JSONArray();
            }

            infoDte.setEstadoDte(EstatusCola.RECHAZADO.name());

            if (observaciones.length() == 0){
                infoDte.setObservaciones(json.optString("descripcionMsg"));
            }else{
                infoDte.setObservaciones(json.optString("observaciones"));
            }

        } catch (DTEException e) {
            throw new DTEException("Hubo un problema al autenticarse en los servicios del Ministerio de Hacienda");
        } catch (Exception e) {
            throw new RuntimeException("Hubo un error inesperado al tratar de invalidar el DTE");
        } finally {
            dteTransaccionService.save(infoDte);
            transaccionService.actualizarTransaccion(transaccion);
        }
        return data;
    }



    public ComprobantePagosDTO consultarComprobante(String numeroComprobante) throws DTEException {
        String url = consultaComprobantePago + numeroComprobante;
        ResponseEntity<ComprobantePagosDTO> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(url, ComprobantePagosDTO.class);
        } catch (HttpClientErrorException e) {
            throw new DTEException("Error al consultar el comprobante: " + e.getMessage());
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new DTEException("Error al consultar el comprobante. HTTP Status: " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

}
