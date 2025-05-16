package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.gov.cnr.cnrpos.entities.MasterUser;
import java.util.Optional;

public interface MasterUserRepository extends JpaRepository<MasterUser, Long> {
    Optional<MasterUser> findByUsuario(String usuario);
}
