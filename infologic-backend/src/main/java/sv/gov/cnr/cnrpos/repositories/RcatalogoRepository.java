package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.RCatalogos;

import java.util.List;
import java.util.Optional;

@Repository
public interface RcatalogoRepository extends JpaRepository<RCatalogos, Long> {

    List<RCatalogos> findByGrupo(String grupo);

    List<RCatalogos> findByIdCatalogo(Long id);

    Optional<RCatalogos> findByGrupoAndValor(String grupo, String valor);

    // Nueva consulta filtrando municipios por ID_MH_PADRE
    @Query("SELECT r FROM RCatalogos r WHERE r.grupo = 'MUNICIPIOS' AND r.idMhPadre = :departamento")
    List<RCatalogos> buscarMunicipiosPorDepartamento(@Param("departamento") String departamento);
}
