package sv.gov.cnr.factelectrcnrservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.ComprobantePago;
import sv.gov.cnr.factelectrcnrservice.exceptions.DTEException;
import sv.gov.cnr.factelectrcnrservice.models.dto.EspecificoCP;
import sv.gov.cnr.factelectrcnrservice.repositories.ComprobantePagoRepository;
import sv.gov.cnr.factelectrcnrservice.repositories.FuncionRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ComprobantePagoService {

    private final ComprobantePagoRepository comprobantePagoRepository;
    private final FuncionRepository funcionRepository;

    @Autowired
    public ComprobantePagoService(ComprobantePagoRepository comprobantePagoRepository, FuncionRepository funcionRepository) {
        this.comprobantePagoRepository = comprobantePagoRepository;
        this.funcionRepository = funcionRepository;
    }

    public List<ComprobantePago> listComprobanteTransaccion(Long idTransaccion) {
        return comprobantePagoRepository.findComprobantePagoByidTransaccion(idTransaccion);
    }

    public EspecificoCP cambioEspecificoCP(String numeroComprobante) throws DTEException {
        try {
            Optional<ComprobantePago> comprobantePago = comprobantePagoRepository.findByNumeroComprobante(numeroComprobante);
            if (comprobantePago.isPresent()) {
                ComprobantePago cp = comprobantePago.get();

                EspecificoCP especificoCP = new EspecificoCP();
                especificoCP.setCodigo(cp.getIdComprobante() != null ? cp.getIdComprobante().intValue() : 0);
                especificoCP.setMensaje("Comprobante encontrado");
                especificoCP.setCp(cp.getNumeroComprobante() != null ? cp.getNumeroComprobante() : "No disponible");
                especificoCP.setDigitado(cp.getRequestEspecifico() != null ? cp.getRequestEspecifico() : "No disponible");
                especificoCP.setActual("Estado actual desconocido");

                return especificoCP;
            } else {
                log.error("No se encontró el comprobante de pago con número: {}", numeroComprobante);
                throw new DTEException("No se encontró el comprobante de pago con número: " + numeroComprobante);
            }
        } catch (Exception e) {
            log.error("Error inesperado al obtener el comprobante de pago: {}", e.getMessage());
            throw new DTEException("Ocurrió un error inesperado: " + e.getMessage(), e);
        }
    }

    public ComprobantePago save(ComprobantePago comprobantePago) {
        return comprobantePagoRepository.save(comprobantePago);
    }
}
