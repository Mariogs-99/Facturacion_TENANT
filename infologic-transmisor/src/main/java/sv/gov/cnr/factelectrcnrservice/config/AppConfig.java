package sv.gov.cnr.factelectrcnrservice.config;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "configuracion")
@Data
@EnableRetry
public class AppConfig {
    @Pattern(regexp = "^(00|01)$", message = "El valorPermitido debe ser '00' o '01'")

    private String ambiente;
    //Manual Técnico para la Integración Tecnológica del Sistema de Transmisión, pagina 14:
    // "La generación del token de seguridad es una vez cada  24 hrs."
    private int horasToken = 24;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(8000))
                .setReadTimeout(Duration.ofMillis(8000))
                .build();
    }
}
