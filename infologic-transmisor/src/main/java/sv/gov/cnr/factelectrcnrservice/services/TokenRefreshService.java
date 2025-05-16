package sv.gov.cnr.factelectrcnrservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
@Slf4j
public class TokenRefreshService {

    @Value("${spring.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.oauth2.authorized-client.google.refresh-token}")
    private String refreshToken;

    private Instant refreshTokenExpiration = Instant.now().plusSeconds(604799); // 7 días por defecto

    // Ejecutar cada 12 horas para verificar si el refresh token está por expirar
    @Scheduled(cron = "0 0 */12 * * ?") // Cada 12 horas
    public void verificarYRenovarToken() {
        if (isRefreshTokenExpiring()) {
            log.warn("El refresh token está por expirar. Intentando regenerarlo...");
            try {
                String nuevoRefreshToken = regenerarRefreshToken();
                if (nuevoRefreshToken != null) {
                    log.info("Refresh token renovado exitosamente.");
                    refreshToken = nuevoRefreshToken;
                    refreshTokenExpiration = Instant.now().plusSeconds(604799);
                    actualizarYml(nuevoRefreshToken);
                } else {
                    log.error("No se pudo renovar el refresh token.");
                }
            } catch (Exception e) {
                log.error("Error al intentar regenerar el refresh token: {}", e.getMessage());
            }
        }
    }

    private boolean isRefreshTokenExpiring() {
        long segundosRestantes = refreshTokenExpiration.getEpochSecond() - Instant.now().getEpochSecond();
        return segundosRestantes < 86400; // Si faltan menos de 24 horas, renovar
    }

    private String regenerarRefreshToken() throws IOException {
        String url = "https://oauth2.googleapis.com/token";
        String params = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&refresh_token=" + refreshToken +
                "&grant_type=refresh_token";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url, null, String.class, params);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        if (jsonNode.has("refresh_token")) {
            return jsonNode.get("refresh_token").asText();
        } else {
            log.error("No se recibió un nuevo refresh token en la respuesta de Google.");
            return null;
        }
    }

    private void actualizarYml(String nuevoRefreshToken) {
        try {
            File configFile = new File("src/main/resources/application.yml");

            if (!configFile.exists()) {
                log.error("El archivo application.yml no existe. No se puede actualizar el refresh token.");
                return;
            }

            String contenidoActual = new String(java.nio.file.Files.readAllBytes(configFile.toPath()));
            String nuevoContenido = contenidoActual.replaceAll(
                    "(refresh-token: )\"[^\"]+\"",
                    "$1\"" + nuevoRefreshToken + "\""
            );

            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write(nuevoContenido);
            }

            log.info("Refresh token actualizado en application.yml.");

            refrescarConfiguracion();
        } catch (IOException e) {
            log.error("Error al actualizar el refresh token en application.yml: " + e.getMessage());
        }
    }

    private void refrescarConfiguracion() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject("http://localhost:8080/actuator/refresh", null, String.class);
            log.info("Configuración recargada exitosamente.");
        } catch (Exception e) {
            log.error("Error al refrescar la configuración de Spring Boot: " + e.getMessage());
        }
    }
}
