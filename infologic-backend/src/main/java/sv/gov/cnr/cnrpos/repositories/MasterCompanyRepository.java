package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.gov.cnr.cnrpos.entities.MasterCompany;

public interface MasterCompanyRepository extends JpaRepository<MasterCompany, Long> {
}
