package sv.gov.cnr.factelectrcnrservice.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.factelectrcnrservice.models.dto.EspecificoCP;

@Repository
@Log4j2
public class FuncionRepository {

    @PersistenceContext
    private EntityManager em;

    public EspecificoCP cambioEspecificoCP(String comprobantePago) {
        Query query = em.createNativeQuery("SELECT ING_DTE_MODESP_CP(?) as jsonResult FROM DUAL");
        query.setParameter(1, comprobantePago);
        String jsonResult = (String) query.getSingleResult();

        ObjectMapper objectMapper = new ObjectMapper();
        EspecificoCP especificoCP = null;
        try {
            especificoCP = objectMapper.readValue(jsonResult, EspecificoCP.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return especificoCP;
    }
}
