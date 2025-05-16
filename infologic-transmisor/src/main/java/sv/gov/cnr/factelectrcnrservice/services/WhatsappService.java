package sv.gov.cnr.factelectrcnrservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sv.gov.cnr.factelectrcnrservice.models.dto.WhatsAppMessageRequest;
import sv.gov.cnr.factelectrcnrservice.models.dto.WhatsAppMessageResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsappService {

    @Autowired
    private RestTemplate restTemplate;

    public WhatsAppMessageResponse sendMessage(WhatsAppMessageRequest request, String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<WhatsAppMessageRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<WhatsAppMessageResponse> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    WhatsAppMessageResponse.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            return handleHttpClientErrorException(e);

    } catch (Exception e) {
            log.error("Error al enviar mensaje por whatsapp");
            return null;
        }
    }

    private WhatsAppMessageResponse handleHttpClientErrorException(HttpClientErrorException e) {
        WhatsAppMessageResponse response = new WhatsAppMessageResponse();
        WhatsAppMessageResponse.WhatsAppError error = new WhatsAppMessageResponse.WhatsAppError();
        if (e.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)){
            error.setMessage(e.getStatusText());
        }else{
            error.setMessage(e.getResponseBodyAsString());
        }
        error.setCode(e.getStatusCode().value());

        response.setError(error);
        return response;
    }

}
