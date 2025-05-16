package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.factelectrcnrservice.entities.DteTransaccion;

@Repository
public interface DteTransaccionRepository extends JpaRepository<DteTransaccion, Long> {

     @Query(value = "SELECT * FROM dte_transaccion " +
             "WHERE id_transaccion = :transaccion ORDER BY id_dte_transaccion DESC LIMIT 1",
             nativeQuery = true)
     DteTransaccion findFirstByTransaccionOrderByIdDteTransaccionDesc(Long transaccion);

     @Query(value = "SELECT dte.*, " +
             "       (CASE WHEN EXISTS (SELECT 1 " +
             "                          FROM dte_transaccion " +
             "                          WHERE id_transaccion = :transaccion AND estado_dte = 'ANULADO') " +
             "             THEN 1 " +
             "             ELSE 0 " +
             "        END) AS anulado " +
             "FROM dte_transaccion dte " +
             "WHERE id_transaccion = :transaccion AND estado_dte = 'PROCESADO' " +
             "ORDER BY id_dte_transaccion DESC " +
             "LIMIT 1",
             nativeQuery = true)
     DteTransaccion getTransaccionForReport(Long transaccion);

     @Query(value = "SELECT COUNT(1) FROM dte_transaccion " +
             "WHERE id_transaccion = :transaccion AND estado_dte = 'ANULADO'",
             nativeQuery = true)
     Long dteAnuladoByTransaccion(Long transaccion);
}
