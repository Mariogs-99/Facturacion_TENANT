package sv.gov.cnr.cnrpos.repositories.security;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.models.security.Rol;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    @Override
    @Query("SELECT r FROM Rol r LEFT JOIN FETCH r.permission p " +
            "WHERE r.deletedAt IS NULL AND (p IS NULL OR p.deletedAt IS NULL)")
    List<Rol> findAll();

    @Override
    @Query("SELECT r FROM Rol r LEFT JOIN FETCH r.permission p " +
            "WHERE r.deletedAt IS NULL AND (p IS NULL OR p.deletedAt IS NULL)")
    Page<Rol> findAll(Pageable page);

    @Override
    @Query("SELECT r FROM Rol r LEFT JOIN FETCH r.permission p " +
            "WHERE r.idRole = :id AND r.deletedAt IS NULL " +
            "AND (p IS NULL OR p.deletedAt IS NULL)")
    Optional<Rol> findById(Long id);
}
