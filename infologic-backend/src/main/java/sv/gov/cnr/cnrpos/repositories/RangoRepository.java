package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sv.gov.cnr.cnrpos.entities.Rango;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RangoRepository extends JpaRepository<Rango, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Rango c SET c.deletedAt = :deletedAt WHERE c.idRango = :id")
    int softDeleteRangoById(Long id, Timestamp deletedAt);

    List<Rango> findByIdSucursal(Long idSucursal);
    List<Rango> findByIdSucursalAndActive(Long idSucursal, Boolean active);
}
