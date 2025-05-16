package sv.gov.cnr.cnrpos.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.DocumentoAsociado;

@Repository
public interface DocumentoAsociadoRepository extends JpaRepository<DocumentoAsociado, Long> {

    @Query("SELECT d.transaccion.id FROM DocumentoAsociado d WHERE d.transaccion.id IS NOT NULL")
    List<Long> findAllTransactionIds();

    @Query("SELECT d.transaccionHija.id FROM DocumentoAsociado d WHERE d.transaccionHija IS NOT NULL")
    List<Long> findAllTransactionIdsHijas();
}
