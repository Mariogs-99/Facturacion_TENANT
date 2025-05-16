package sv.gov.cnr.factelectrcnrservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.repositories.RangoRepository;

@Service
@RequiredArgsConstructor
public class RangoService {

    private final RangoRepository rangoRepository;

    public Integer findRangoActivoPorDte(Integer tipoDte, Long idSucursal){
        Integer correlativo = rangoRepository.findRangoActivoPorDte(tipoDte, idSucursal);
        if(correlativo == null){
            throw new IllegalArgumentException("No existe un rango activo para dicho tipo de DTE");
        }
        return correlativo + 1;
    }

    public Integer updateRango(Integer tipoDte, Integer valorActual, Long idSucursal) {
        return rangoRepository.updateRangoActivoPorDte(tipoDte, valorActual, idSucursal);
    }
}
