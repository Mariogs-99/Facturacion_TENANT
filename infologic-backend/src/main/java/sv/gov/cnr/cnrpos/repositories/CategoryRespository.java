package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.RCatalogos;

import java.util.List;

@Repository
public interface CategoryRespository extends JpaRepository<RCatalogos, Long> {

    @Query("SELECT c FROM RCatalogos c WHERE c.grupo = :grupo")
    List<RCatalogos> findByGrupo(@Param("grupo") String grupo);

    @Query("SELECT c FROM RCatalogos c WHERE c.idCatalogo IN :ids")
    List<RCatalogos> findByIds(@Param("ids") List<Long> ids);
}
