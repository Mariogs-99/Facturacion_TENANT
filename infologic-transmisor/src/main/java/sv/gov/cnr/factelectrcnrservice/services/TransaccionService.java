package sv.gov.cnr.factelectrcnrservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.repositories.TransaccionRepository;

@Service
@RequiredArgsConstructor
public class TransaccionService {
    private final TransaccionRepository transaccionRepository;
    public Transaccion actualizarTransaccion(Transaccion transaccion){
        return transaccionRepository.saveAndFlush(transaccion);
    }

    public Transaccion findByCodigoGeneracion(String codigoGeneracion) {
        return transaccionRepository.findByCodigoGeneracion(codigoGeneracion);
    }
}
