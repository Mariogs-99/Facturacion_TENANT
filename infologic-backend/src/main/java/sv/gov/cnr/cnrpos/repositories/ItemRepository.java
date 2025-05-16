package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT it.* " +
            "FROM transaccion tr " +
            "INNER JOIN item it ON it.id_transaccion = tr.id_transaccion " +
            "WHERE tr.codigo_generacion = :codigoGeneracion", nativeQuery = true)
    List<Item> itemsByDte(@Param("codigoGeneracion") String codigoGeneracion);

    @Query(value = "SELECT it.* " +
            "FROM transaccion tr " +
            "INNER JOIN item it ON it.id_transaccion = tr.id_transaccion " +
            "WHERE it.nro_documento = :codigoGeneracion AND tr.status = 2 " +
            "AND tr.tipo_dte = '5' ", nativeQuery = true)
    List<Item> itemsUsadosNotas(@Param("codigoGeneracion") String codigoGeneracion);

    @Query(value = "SELECT it.* " +
            "FROM transaccion tr " +
            "INNER JOIN item it ON it.id_transaccion = tr.id_transaccion " +
            "WHERE tr.codigo_generacion IN :codigosGeneracion", nativeQuery = true)
    List<Item> itemsByDtes(@Param("codigosGeneracion") List<String> codigosGeneracion);
}
