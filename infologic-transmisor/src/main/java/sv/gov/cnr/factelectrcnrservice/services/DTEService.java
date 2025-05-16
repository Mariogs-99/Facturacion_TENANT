package sv.gov.cnr.factelectrcnrservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.Cola;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.exceptions.DTEException;
import sv.gov.cnr.factelectrcnrservice.factory.DTEFactory;
import sv.gov.cnr.factelectrcnrservice.models.dto.MotivoAnulacionDTO;
import sv.gov.cnr.factelectrcnrservice.models.enums.TipoDTE;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DTEService {

    private final DTEFactory dteFactory;
    private final HaciendaApiService haciendaApiService;
    private final ReporteService reporteService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final ColaService colaService;
    private final DteTransaccionService dteTransaccionService;
    private final DocumentosCNRServices documentosCNRServices;

    /**
     * Crea un DTE para la transacción dada.
     * @param transaccion transacción en cola para crear DTE
     * @return objeto DTE generado
     * @throws Exception error al generar instancia de DTE
     */
    public Object crearDTE(Transaccion transaccion) throws Exception {
        log.info("Creando DTE para la transacción: #{}", transaccion.getIdTransaccion());
        var objetoDte = crearJSON(transaccion);
        guardarJson(transaccion, objetoDte);
        return objetoDte;
    }

    /**
     * Firma un DTE con Hacienda.
     * @param transaccion transacción en cola para ser firmada
     * @param dte objeto DTE a firmar
     * @throws DTEException error al firmar DTE
     * @throws JsonProcessingException error al parsear JSON
     */
    public void firmarDte(Transaccion transaccion, Object dte) throws DTEException, JsonProcessingException {
        log.info("Firmando DTE para la transacción: #{}", transaccion.getIdTransaccion());
        var jsonFirmado = haciendaApiService.firmarDocumento(dte);
        guardarJsonFirmado(transaccion, jsonFirmado);
    }

    public String transmitirDte(Transaccion transaccion) throws DTEException {
        log.info("Transmitiendo DTE a Hacienda para la transacción: #{}", transaccion.getIdTransaccion());
        return haciendaApiService.enviarDTE(transaccion);
    }

    public String consultarDte(Transaccion transaccion) {
        try {
            return haciendaApiService.consultarDte(transaccion);
        } catch (DTEException e) {
            throw new RuntimeException("No fue posible realizar la consulta del DTE", e);
        }
    }

    /**
     * Envía el DTE al receptor vía correo y lo sube a CNR.
     * @param transaccion transacción que contiene el DTE
     * @throws Exception error al enviar el DTE
     */
    public void enviarDte(Transaccion transaccion) throws Exception {
        log.info("Enviando DTE al receptor para la transacción: #{}", transaccion.getIdTransaccion());
        ByteArrayOutputStream dtePdf = reporteService.generateReportToStream(transaccion);

        boolean emailEnviado = emailService.sendDteViaEmail(transaccion, dtePdf, transaccion.getCliente().getEmail());
        if (emailEnviado) {
            log.info("Correo enviado correctamente para la transacción: #{}", transaccion.getIdTransaccion());
        } else {
            log.warn("No se pudo enviar el correo para la transacción: #{}", transaccion.getIdTransaccion());
        }

        //documentosCNRServices.subirArchivo(dtePdf, transaccion);
    }

    /**
     * Envía el DTE anulado al receptor vía correo y lo sube a CNR.
     * @param transaccion transacción con DTE anulado
     * @throws Exception error al enviar el DTE anulado
     */
    public void enviarDteAnulado(Transaccion transaccion) throws Exception {
        log.info("Enviando DTE anulado al receptor para la transacción: #{}", transaccion.getIdTransaccion());
        ByteArrayOutputStream dtePdf = reporteService.generateReportToStream(transaccion);

        boolean emailEnviado = emailService.sendDteViaEmailAnulado(transaccion, dtePdf, transaccion.getCliente().getEmail());
        if (emailEnviado) {
            log.info("Correo de DTE anulado enviado correctamente para la transacción: #{}", transaccion.getIdTransaccion());
        } else {
            log.warn("No se pudo enviar el correo de DTE anulado para la transacción: #{}", transaccion.getIdTransaccion());
        }

        //documentosCNRServices.subirArchivo(dtePdf, transaccion);
    }

    public String notificarContigencia(Transaccion transaccion) throws DTEException {
        return haciendaApiService.notificarContingencia(transaccion);
    }

    /**
     * Guarda el JSON del DTE en la base de datos.
     * @param transaccion transacción asociada al DTE
     * @param dte objeto DTE en formato JSON
     * @throws JsonProcessingException error al serializar JSON
     */
    private void guardarJson(Transaccion transaccion, Object dte) throws JsonProcessingException {
        DteTransaccion infoDte = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        infoDte.setDteJson(objectMapper.writeValueAsString(dte));
        dteTransaccionService.save(infoDte);
    }

    /**
     * Guarda el JSON firmado del DTE en la base de datos.
     * @param transaccion transacción asociada al DTE firmado
     * @param jsonFirmado JSON firmado del DTE
     */
    private void guardarJsonFirmado(Transaccion transaccion, String jsonFirmado) {
        DteTransaccion infoDte = dteTransaccionService.findDteTransaccionByTransaccion(transaccion);
        infoDte.setDteJsonFirmado(jsonFirmado);
        dteTransaccionService.save(infoDte);
    }

    /**
     * Crea el JSON del DTE basándose en la transacción.
     * @param transaccion transacción de la que se generará el DTE
     * @return objeto DTE en JSON
     * @throws Exception error al crear el JSON
     */
    private Object crearJSON(Transaccion transaccion) throws Exception {
        Cola cola = colaService.obtenerInfoCola(transaccion);
        TipoDTE tipoDte;

        if (Boolean.TRUE.equals(cola.getEsContingencia()) && Boolean.FALSE.equals(cola.getNotificadoContigencia())) {
            tipoDte = TipoDTE.CONTINGENCIA;
        } else {
            tipoDte = TipoDTE.obtenerPorCodigo(transaccion.getTipoDTE());
        }

        var dte = dteFactory.fabricarObjetoDTE(tipoDte);
        return dte.crearDTEJSON(transaccion);
    }

    public Map<String, Object> invalidarDte(Transaccion transaccion, MotivoAnulacionDTO dataMotivo) throws DTEException, JsonProcessingException {
        var dte = dteFactory.fabricarObjetoDTE(TipoDTE.INVALIDACION);
        var jsonInvalidacion = dte.crearJsonInvalidacion(transaccion, dataMotivo);
        guardarJson(transaccion, jsonInvalidacion);
        firmarDte(transaccion, jsonInvalidacion);
        return haciendaApiService.invalidarDte(transaccion);
    }
}
