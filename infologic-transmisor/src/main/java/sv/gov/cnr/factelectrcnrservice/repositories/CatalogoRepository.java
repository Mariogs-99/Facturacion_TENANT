package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.factelectrcnrservice.entities.RCatalogos;

import java.util.Optional;

@Repository
public interface CatalogoRepository extends JpaRepository<RCatalogos,Long> {
    Optional<RCatalogos> findByCatPadreAndValorIgnoreCase(String catPadre, String valor);
    Optional<RCatalogos> findByIdMhAndCatPadre(String idMh, String catPadre);

    Optional<RCatalogos> findByCatPadreAndIdMhIgnoreCase(String catPadre, String valor);
    Optional<RCatalogos> findByCatPadreAndIdMhAndIdMhPadreIgnoreCase(String catPadre, String valor, String valor2);



}
