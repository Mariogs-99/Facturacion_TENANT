package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.Producto;
import sv.gov.cnr.cnrpos.models.security.User;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>, PagingAndSortingRepository<Producto, Integer> {
    Page<Producto> findAll(Pageable pageable);
}
