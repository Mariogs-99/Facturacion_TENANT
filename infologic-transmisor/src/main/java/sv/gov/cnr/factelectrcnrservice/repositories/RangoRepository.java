package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sv.gov.cnr.factelectrcnrservice.entities.Rango;

@Repository
public interface RangoRepository extends JpaRepository<Rango, Long> {

    @Query(value = "SELECT r.actual_valor FROM rango r " +
            "WHERE r.active = 1 AND r.id_tipo_rango = :tipoDte " +
            "AND r.id_sucursal = :idSucursal " +
            "AND r.actual_valor + 1 BETWEEN r.inicio_rango AND r.final_rango",
            nativeQuery = true)
    Integer findRangoActivoPorDte(@Param("tipoDte") Integer tipoDte, @Param("idSucursal") Long idSucursal);

    @Modifying
    @Transactional
    @Query(value = "UPDATE rango r SET r.actual_valor = :nuevoValor " +
            "WHERE r.active = 1 AND r.id_tipo_rango = :tipoDte " +
            "AND r.id_sucursal = :idSucursal",
            nativeQuery = true)
    Integer updateRangoActivoPorDte(@Param("tipoDte") Integer tipoDte,
                                    @Param("nuevoValor") Integer nuevoValor,
                                    @Param("idSucursal") Long idSucursal);
}
