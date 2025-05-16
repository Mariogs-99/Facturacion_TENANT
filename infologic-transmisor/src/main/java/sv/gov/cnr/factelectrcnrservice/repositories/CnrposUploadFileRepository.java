package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.factelectrcnrservice.entities.CnrposUploadFile;

import java.util.Optional;

@Repository
public interface CnrposUploadFileRepository extends JpaRepository<CnrposUploadFile, Long> {

    Optional<CnrposUploadFile> findFirstByEstadoEquals(String estado);
}
