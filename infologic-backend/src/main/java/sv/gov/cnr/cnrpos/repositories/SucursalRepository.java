package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sv.gov.cnr.cnrpos.entities.Sucursal;
import sv.gov.cnr.cnrpos.models.SucursalProjection;
import sv.gov.cnr.cnrpos.models.dto.SucursalDTO;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Sucursal c SET c.deletedAt = :deletedAt WHERE c.idSucursal = :id")
    int softDeleteSucursalById(Long id, Timestamp deletedAt);

    List<Sucursal> findAllByDeletedAtIsNull();

    @Query("SELECT s FROM Sucursal s WHERE s.idCompany = :id AND s.deletedAt IS NULL")
    List<Sucursal> getSucursalByCompanyId(@Param("id") Long id);

    @Query("SELECT s FROM Sucursal s WHERE s.idSucursal = :id AND s.deletedAt IS NULL")
    Sucursal findByIdDeletedAtIsNull(@Param("id") Long id);

    @Query(value = "SELECT SUBSTR(s.codigo_sucursal, 1, 4) AS ID, " +
            "MAX(s.nombre_sucursal) AS nombreSucursal, " +
            "COUNT(*) AS numeroSucursales " +
            "FROM sucursal s " +
            "GROUP BY SUBSTR(s.codigo_sucursal, 1, 4)",
            nativeQuery = true)
    List<SucursalProjection> findSucursalCounts();

    @Query(value = "SELECT s.modulo FROM sucursal s GROUP BY s.modulo",
            nativeQuery = true)
    List<String> modulosSucursales();

    @Query("SELECT new sv.gov.cnr.cnrpos.models.dto.SucursalDTO(s.idSucursal, CONCAT(s.nombre, ' - ', s.misional)) " +
            "FROM Sucursal s")
    List<SucursalDTO> findSucursalesWithConcat();
}
