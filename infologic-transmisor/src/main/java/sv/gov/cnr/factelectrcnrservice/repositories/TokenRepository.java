package sv.gov.cnr.factelectrcnrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.factelectrcnrservice.entities.TokenMh;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenMh, String> {

    @Query(value = "SELECT * FROM token_mh t " +
            "WHERE t.FECHA_GENERACION >= :cutoffTime " +
            "ORDER BY t.FECHA_GENERACION DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<TokenMh> findRecentToken(@Param("cutoffTime") Timestamp cutoffTime);
}
