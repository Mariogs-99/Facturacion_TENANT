package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.gov.cnr.factelectrcnrservice.entities.Cola;
import sv.gov.cnr.factelectrcnrservice.entities.Transaccion;

import java.util.Optional;

public interface ColaRepository extends JpaRepository<Cola, Long> {

    @Query(value = "SELECT * FROM cola " +
            "WHERE (nro_intento < 3 OR NOTIFICADO_CONTIGENCIA = 1) AND FINALIZADO = 0 " +
            "ORDER BY FECHA_HORA ASC, nro_intento DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Cola> obtenerRegistroOperacionNormal();

    @Query(value = "SELECT c.* FROM cola c " +
            "INNER JOIN transaccion t ON t.ID_TRANSACCION = c.ID_COLA " +
            "WHERE c.ES_CONTINGENCIA = 1 AND c.FINALIZADO = 0 AND c.NOTIFICADO_CONTIGENCIA = 0 " +
            "AND t.TIPO_DTE NOT IN ('07', '08') " +
            "ORDER BY c.FECHA_HORA ASC, c.nro_intento DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Cola> obtenerRegistroOperacionContingencia();

    Cola findByTransaccion(Transaccion transaccion);
}
