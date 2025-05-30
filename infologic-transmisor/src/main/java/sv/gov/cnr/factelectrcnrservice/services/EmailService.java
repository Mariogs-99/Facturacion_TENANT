package sv.gov.cnr.factelectrcnrservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final DteTransaccionService dteTransaccionService;
    private final TemplateEngine templateEngine;

    @Value("${MAIL_USERNAME}")
    private String correoEmisor;

    @Value("${CLIENTE_GOOGLE}")
    private String clientId;

    @Value("${CLIENTE_SECRET}")
    private String clientSecret;

    @Value("${REFRESH_TOKEN}")
    private String refreshToken;


    @Value("${configuracion.ambiente}")
    private String ambiente;

    public static final String ASUNTO = "FACTURACIÓN ELECTRÓNICA INFOLOGIC";
    public static final String TEXT_HTML_ENCODING = "text/html";
    public static final String EMAIL_TEMPLATE = "plantillaEmail";

    public Boolean sendDteViaEmail(Transaccion transaccion, ByteArrayOutputStream dtePDF, String correo) {
        try {
            if (correo == null) {
                correo = correoEmisor;
            }

            DteTransaccion infoDte = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
            String accessToken = obtenerAccessToken();

            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.auth.mechanisms", "XOAUTH2");

            Session session = Session.getInstance(prop, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoEmisor, accessToken);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoEmisor));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo));
            message.setSubject(ASUNTO);

            Context context = new Context();
            context.setVariable("textAnulado", "");
            if ("00".equalsIgnoreCase(ambiente)) {
                context.setVariable("msjPrueba", "Este correo ha sido enviado con el propósito de realizar pruebas.");
            } else {
                context.setVariable("msjPrueba", "");
            }

            MimeBodyPart leyenda = new MimeBodyPart();
            String bodyEmail = templateEngine.process(EMAIL_TEMPLATE, context);
            leyenda.setContent(bodyEmail, TEXT_HTML_ENCODING);

            MimeBodyPart imageBodyPart = new MimeBodyPart();
            try {
                Resource resource = new ClassPathResource("templates/Logo-info.png");
                InputStream imageStream = resource.getInputStream();
                DataSource dataSource = new ByteArrayDataSource(imageStream, "image/png");
                imageBodyPart.setDataHandler(new DataHandler(dataSource));
                imageBodyPart.setHeader("Content-ID", "image");
            } catch (IOException | MessagingException e) {
                log.error("Error al incrustar imagen en el correo: " + e.getMessage());
            }

            MimeBodyPart pdfDteAdjunto = new MimeBodyPart();
            ByteArrayDataSource pdfDataSource = new ByteArrayDataSource(dtePDF.toByteArray(), "application/pdf");
            pdfDteAdjunto.setDataHandler(new DataHandler(pdfDataSource));
            pdfDteAdjunto.setFileName(infoDte.getCodigoGeneracion() + ".pdf");

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(infoDte.getDteJson(), LinkedHashMap.class);
            map.put("firma", infoDte.getDteJsonFirmado());
            map.put("sello", infoDte.getSelloRecibidoMh());

            MimeBodyPart jsonDteAdjunto = new MimeBodyPart();
            ByteArrayDataSource jsonDataSource = new ByteArrayDataSource(
                    objectMapper.writeValueAsString(map).getBytes(StandardCharsets.UTF_8), "application/json");
            jsonDteAdjunto.setDataHandler(new DataHandler(jsonDataSource));
            jsonDteAdjunto.setFileName(infoDte.getCodigoGeneracion() + ".json");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(imageBodyPart);
            multipart.addBodyPart(leyenda);
            multipart.addBodyPart(pdfDteAdjunto);
            multipart.addBodyPart(jsonDteAdjunto);

            message.setContent(multipart);

            Transport.send(message);
            log.info("Correo enviado exitosamente");
            return true;
        } catch (Exception e) {
            log.error("Error al enviar correo: " + e.getMessage());
            return false;
        }
    }

    private String obtenerAccessToken() throws IOException {
        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build();

        AccessToken token = credentials.refreshAccessToken();
        return token.getTokenValue();
    }

    public Boolean sendDteViaEmailAnulado(Transaccion transaccion, ByteArrayOutputStream dtePDF, String correo) {
        return sendDteViaEmail(transaccion, dtePDF, correo); // Reutiliza el método de envío normal
    }

}
