package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.gov.cnr.factelectrcnrservice.entities.ComprobantePago;

import java.util.List;
import java.util.Optional;

public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {

    List<ComprobantePago> findComprobantePagoByidTransaccion(Long idTransaccion);
    Optional<ComprobantePago> findByNumeroComprobante(String numeroComprobante);
}


