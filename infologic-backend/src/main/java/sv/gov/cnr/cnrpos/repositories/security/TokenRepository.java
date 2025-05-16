package sv.gov.cnr.cnrpos.repositories.security;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.models.security.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
      SELECT t FROM Token t 
      INNER JOIN t.user u
      WHERE u.id = :id AND (t.expired = false OR t.revoked = false)
      """)
    List<Token> findAllValidTokenByUser(@Param("id") Long id);

    Optional<Token> findByToken(String token);
}
