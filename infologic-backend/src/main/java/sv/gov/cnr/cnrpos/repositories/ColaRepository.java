package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.Cola;

@Repository
public interface ColaRepository extends JpaRepository<Cola,Long> {
}
