package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.RCatalogos;

import java.util.Optional;

@Repository
public interface CatalogoRepository extends JpaRepository<RCatalogos,Long> {
    Optional<RCatalogos> findByCatPadreAndValorIgnoreCase(String catPadre, String valor);
}
