package sv.gov.cnr.cnrpos.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.Cola;
import sv.gov.cnr.cnrpos.entities.Transaccion;
import sv.gov.cnr.cnrpos.models.enums.EstatusCola;
import sv.gov.cnr.cnrpos.repositories.ColaRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ColaService {
    private final ColaRepository colaRepository;
    public void encolarTransaccion(Transaccion transaccion){
        var transaccionEnCola = Cola.builder()
                .transaccion(transaccion)
                .estatusCola(EstatusCola.PENDIENTE)
                .nroIntento(0)
                .nroIntentoCont(0)
                .esContingencia(Boolean.FALSE)
                .notificadoContigencia(Boolean.FALSE)
                .finalizado(Boolean.FALSE)
                .build();
        var elemento = colaRepository.save(transaccionEnCola);
        log.info("Se agregó un nuevo elemento a la cola. {}",elemento);
    }

}
