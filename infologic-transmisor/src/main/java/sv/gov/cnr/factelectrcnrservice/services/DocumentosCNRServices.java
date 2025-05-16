package sv.gov.cnr.factelectrcnrservice.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sv.gov.cnr.factelectrcnrservice.entities.CnrposUploadFile;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.models.dto.AuxUploadFilesDTO;
import sv.gov.cnr.factelectrcnrservice.models.dto.CnrposUploadFileResponseDTO;
import sv.gov.cnr.factelectrcnrservice.models.enums.EstatusCola;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

@Service
@Slf4j
public class DocumentosCNRServices {

    private final CnrposUploadFileService cnrposUploadFileService;
    private final DteTransaccionService dteTransaccionService;
    private final RestTemplate restTemplate;

    public DocumentosCNRServices(CnrposUploadFileService cnrposUploadFileService, DteTransaccionService dteTransaccionService, RestTemplate restTemplate) {
        this.cnrposUploadFileService = cnrposUploadFileService;
        this.dteTransaccionService = dteTransaccionService;
        this.restTemplate = restTemplate;
    }

//    public void subirArchivo(ByteArrayOutputStream filePDF, Transaccion transaccion) {
//        CnrposUploadFile cnrposUploadFile = cnrposUploadFileService.getActivo().orElse(null);
//        DteTransaccion dteTransaccion = null;
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
//
//        if (cnrposUploadFile == null) {
//            return; // Opcional: lanzar una excepci n o manejar seg n la l gica de tu aplicaci n
//        }
//
//        try {
//            dteTransaccion = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
//            DteTransaccion finalDteTransaccion = dteTransaccion;
//            ByteArrayResource byteArrayResource = new ByteArrayResource(filePDF.toByteArray()) {
//                @Override
//                public String getFilename() {
//                    return (Objects.equals(finalDteTransaccion.getEstadoDte(), EstatusCola.ANULADO.name()))
//                            ? finalDteTransaccion.getCodigoGeneracionAnulacion() + ".pdf"
//                            : finalDteTransaccion.getCodigoGeneracion() + ".pdf";
//                }
//
//                @Override
//                public long contentLength() {
//                    return filePDF.size();
//                }
//            };
//
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("preId", String.valueOf(cnrposUploadFile.getPreID()));
//            body.add("tanId", String.valueOf(cnrposUploadFile.getTanId()));
//            body.add("usuCrea", String.valueOf(cnrposUploadFile.getUsuCrea()));
//            body.add("file", byteArrayResource);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            requestEntity = new HttpEntity<>(body, headers);
//
//            String url = cnrposUploadFile.getUrlApi();
//            ResponseEntity<CnrposUploadFileResponseDTO> response = restTemplate.postForEntity(url, requestEntity, CnrposUploadFileResponseDTO.class);
//
//            dteTransaccion.setRequestApiArchivo(requestEntity.toString());
//            dteTransaccion.setResponseApiArchivo(String.valueOf(response.getBody()));
//
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().getCode() == 200) {
//                dteTransaccion.setIdPdf(response.getBody().getMensaje());
//            }
//
//            dteTransaccionService.save(dteTransaccion);
//
//        } catch (HttpClientErrorException e) {
//            CnrposUploadFileResponseDTO response = e.getResponseBodyAs(CnrposUploadFileResponseDTO.class);
//            if (dteTransaccion != null) {
//                dteTransaccion.setRequestApiArchivo(requestEntity != null ? requestEntity.toString() : "Entidad no disponible");
//                dteTransaccion.setResponseApiArchivo(response != null ? response.toString() : null);
//            }
//            dteTransaccionService.save(dteTransaccion);
//
//        } catch (Exception e) {
//            log.error("Error en método subirArchivo", e);
//        }
//    }

}
