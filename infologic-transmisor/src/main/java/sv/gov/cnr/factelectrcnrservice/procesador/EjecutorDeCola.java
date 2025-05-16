package sv.gov.cnr.factelectrcnrservice.procesador;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sv.gov.cnr.factelectrcnrservice.entities.ComprobantePago;
import sv.gov.cnr.factelectrcnrservice.entities.Cola;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;
import sv.gov.cnr.factelectrcnrservice.entities.MasterCompany;
import sv.gov.cnr.factelectrcnrservice.exceptions.DTEException;
import sv.gov.cnr.factelectrcnrservice.models.dto.EspecificoCP;
import sv.gov.cnr.factelectrcnrservice.models.enums.EstatusCola;
import sv.gov.cnr.factelectrcnrservice.services.*;
import sv.gov.cnr.factelectrcnrservice.utils.Validator;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class EjecutorDeCola {

    private final ColaService colaService;
    private final LockRegistry lockRegistry;
    private final DTEService dteService;
    private final TransaccionService transaccionService;
    private final Validator validator;
    private final DteTransaccionService dteTransaccionService;
    private final ComprobantePagoService comprobantePagoService;
    private final MasterCompanyService masterCompanyService;

    private static final String estadoAprobado = "PROCESADO";
    private static final String contingenciaRecibido = "RECIBIDO";

    public void procesarRegistro(Cola registroEnCola) throws Exception {
        boolean presentado = false;
        var transaccion = registroEnCola.getTransaccion();

        Optional<DteTransaccion> transaccionDTEValidar = dteTransaccionService.findDteTransaccionByTransaccionValidar(transaccion);
        String estadoDTETransaccion = transaccionDTEValidar.map(DteTransaccion::getEstadoDte).orElse(null);

        if (estadoDTETransaccion == null || !estadoDTETransaccion.equalsIgnoreCase(estadoAprobado)) {
            var lock = lockRegistry.obtain(String.valueOf(registroEnCola.getIdCola()));
            if (lock.tryLock()) {
                boolean condicionesMinimas = validator.condicionesMinimas(transaccion);
                try {
                    log.info("Elemento de la cola ID {} bloqueado", registroEnCola.getIdCola());
                    log.info("El siguiente registro en cola es {}", registroEnCola);
                    if (condicionesMinimas) {
                        Object dte = dteService.crearDTE(transaccion);
                        dteService.firmarDte(transaccion, dte);
                        String estado = dteService.transmitirDte(transaccion);
                        if (estado.equals(estadoAprobado)) {
                            presentado = true;
                            transaccion.setStatus(registroEnCola.getEsContingencia() ? EstatusCola.APROBADO_CONTINGENCIA : EstatusCola.APROBADO);
                            registroEnCola.setEstatusCola(EstatusCola.ENVIADO);
                            dteService.enviarDte(transaccion);
                            log.info("Cambio de especifico para la transaccion {}", transaccion.getIdTransaccion());
                            actualizarComprobantePago(transaccion.getIdTransaccion());
                        } else {
                            transaccion.setStatus(EstatusCola.RECHAZADO);
                            registroEnCola.setEstatusCola(EstatusCola.ERROR);
                        }
                        transaccionService.actualizarTransaccion(transaccion);
                        registroEnCola.setFinalizado(true);
                        colaService.save(registroEnCola);
                    } else {
                        log.warn("No se cumplieron las condiciones mínimas para transmitir DTEs");
                        transaccion.setStatus(EstatusCola.RECHAZADO);
                        registroEnCola.setEstatusCola(EstatusCola.ERROR);
                        registroEnCola.setFinalizado(true);
                        transaccionService.actualizarTransaccion(transaccion);
                        colaService.save(registroEnCola);
                    }
                } catch (Exception e) {
                    log.error("Error al enviar el DTE: {}", e.getMessage());
                    log.info("Consulta de DTE");
                    if (!presentado) {
                        var estado = dteService.consultarDte(transaccion);
                        if (estado != null && estado.equals(estadoAprobado)) {
                            transaccion.setStatus(registroEnCola.getEsContingencia() ? EstatusCola.APROBADO_CONTINGENCIA : EstatusCola.APROBADO);
                            registroEnCola.setEstatusCola(EstatusCola.ENVIADO);
                            dteService.enviarDte(transaccion);
                        } else {
                            transaccion.setStatus(EstatusCola.RECHAZADO);
                            registroEnCola.setEstatusCola(EstatusCola.ERROR);
                        }
                        registroEnCola.setFinalizado(true);
                        colaService.save(registroEnCola);
                    } else {
                        registroEnCola.setFinalizado(true);
                    }
                } finally {
                    log.info("Finalizó el proceso");
                    if (condicionesMinimas) {
                        if (!registroEnCola.getEsContingencia()) {
                            registroEnCola.setNroIntento(registroEnCola.getNroIntento() + 1);
                        } else {
                            registroEnCola.setNroIntentoCont(registroEnCola.getNroIntentoCont() + 1);
                        }
                        if (registroEnCola.getNroIntento() == 3 && !registroEnCola.getEsContingencia()) {
                            registroEnCola.setEsContingencia(true);
                            transaccion.setStatus(EstatusCola.MARCADO_CONTINGENCIA);
                        }
                    }
                    colaService.save(registroEnCola);
                    transaccionService.actualizarTransaccion(transaccion);
                    lock.unlock();
                }
            } else {
                log.warn("Lock para el ID {} no adquirido", registroEnCola.getIdCola());
            }
        }
    }

    private void actualizarComprobantePago(Long idTransaccion) throws DTEException {
        List<ComprobantePago> comprobantesPago = comprobantePagoService.listComprobanteTransaccion(idTransaccion);
        for (ComprobantePago cp : comprobantesPago) {
            EspecificoCP especificoCP = comprobantePagoService.cambioEspecificoCP(cp.getNumeroComprobante());
            String jsonResult = serializeToJson(especificoCP);
            cp.setRequestEspecifico(jsonResult);
            comprobantePagoService.save(cp);
        }
    }

    private String serializeToJson(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar el objeto a JSON", e);
        }
    }
}
