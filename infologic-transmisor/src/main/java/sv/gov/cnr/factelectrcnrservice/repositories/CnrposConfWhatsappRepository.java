package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.factelectrcnrservice.entities.CnrposConfWhatsapp;

import java.util.Optional;

@Repository
public interface CnrposConfWhatsappRepository extends JpaRepository<CnrposConfWhatsapp, Long> {
    Optional<CnrposConfWhatsapp> findFirstByEstadoOrderByIdConfDesc(String estado);
}
