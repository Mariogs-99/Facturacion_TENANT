package sv.gov.cnr.factelectrcnrservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sv.gov.cnr.factelectrcnrservice.entities.Cola;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;
import sv.gov.cnr.factelectrcnrservice.repositories.ColaRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ColaService {

    private final ColaRepository colaRepository;

    public void removerDeLaCola(Cola registroEnCola){
        colaRepository.delete(registroEnCola);
    }


    public Cola save(Cola cola){
        return colaRepository.save(cola);
    }

    public Optional<Cola> obtenerRegistroOperacionNormal(){
        return colaRepository.obtenerRegistroOperacionNormal();
    }

    public Optional<Cola> obtenerRegistroOperacionaContingencia(){return colaRepository.obtenerRegistroOperacionContingencia();};


    public Cola obtenerInfoCola(Transaccion transaccion){
        return colaRepository.findByTransaccion(transaccion);
    }


}
