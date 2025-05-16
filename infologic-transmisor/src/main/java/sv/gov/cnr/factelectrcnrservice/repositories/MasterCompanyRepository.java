package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.gov.cnr.factelectrcnrservice.entities.MasterCompany;

import java.util.List;
import java.util.Optional;

public interface MasterCompanyRepository extends JpaRepository<MasterCompany, Long> {
    Optional<MasterCompany> findByNit(String nit);


    List<MasterCompany> findAllByActiveTrue(); // <-- agrega esta línea


}


