package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.gov.cnr.cnrpos.entities.Compra;

import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Override
    @Query(value = "SELECT c FROM Compra c WHERE c.deletedAt IS NULL",
            countQuery = "SELECT COUNT(c) FROM Compra c WHERE c.deletedAt IS NULL")
    Page<Compra> findAll(Pageable page);

    @Override
    @Query("SELECT c FROM Compra c WHERE c.idCompra = :id AND c.deletedAt IS NULL")
    Optional<Compra> findById(Long id);
}
