package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.Cliente;
import sv.gov.cnr.cnrpos.entities.Producto;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
    Page<Cliente> findAll(Pageable pageable);
}
