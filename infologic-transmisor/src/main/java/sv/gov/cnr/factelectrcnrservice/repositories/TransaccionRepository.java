package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion,Long> {
    Transaccion findByCodigoGeneracion(String codigoGeneracion);
}
