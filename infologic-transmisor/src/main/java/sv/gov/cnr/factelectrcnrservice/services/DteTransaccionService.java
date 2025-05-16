package sv.gov.cnr.factelectrcnrservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.repositories.DteTransaccionRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DteTransaccionService {




    private final DteTransaccionRepository dteTransaccionRepository;

    public DteTransaccion save(DteTransaccion dteTransaccion){return dteTransaccionRepository.save(dteTransaccion);}

    public DteTransaccion saveAndFlush(DteTransaccion dteTransaccion){
        dteTransaccion.setIdDteTransaccion(null);
        return  dteTransaccionRepository.saveAndFlush(dteTransaccion);
    }


    public DteTransaccion findDteTransaccionByTransaccion(Transaccion transaccion){
        Long transaccionId = transaccion.getIdTransaccion();
        return dteTransaccionRepository.findFirstByTransaccionOrderByIdDteTransaccionDesc(transaccionId);
    }

    public DteTransaccion findDteTransaccionForReport(Transaccion transaccion){
        Long transaccionId = transaccion.getIdTransaccion();
        return dteTransaccionRepository.getTransaccionForReport(transaccionId);
    }

    public Boolean dteAnuladoByTransaccion(Transaccion transaccion){
        Long transaccionId = transaccion.getIdTransaccion();
        Long count = dteTransaccionRepository.dteAnuladoByTransaccion(transaccionId);
        return count > 0;
    }

    public Optional<DteTransaccion> findDteTransaccionByTransaccionValidar(Transaccion transaccion){
        Long transaccionId = transaccion.getIdTransaccion();
        return Optional.ofNullable(dteTransaccionRepository.findFirstByTransaccionOrderByIdDteTransaccionDesc(transaccionId));
    }

}
