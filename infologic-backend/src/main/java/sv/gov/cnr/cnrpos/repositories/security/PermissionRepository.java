package sv.gov.cnr.cnrpos.repositories.security;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.models.security.Permission;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Override
    @Query("SELECT p FROM Permission p LEFT JOIN FETCH p.menuItems mi " +
            "WHERE p.deletedAt IS NULL AND (mi IS NULL OR mi.deletedAt IS NULL)")
    List<Permission> findAll();

    @Override
    @Query(value = "SELECT p FROM Permission p LEFT JOIN FETCH p.menuItems mi " +
            "WHERE p.deletedAt IS NULL AND (mi IS NULL OR mi.deletedAt IS NULL)",
            countQuery = "SELECT COUNT(p) FROM Permission p WHERE p.deletedAt IS NULL")
    Page<Permission> findAll(Pageable page);

    @Override
    @Query("SELECT p FROM Permission p LEFT JOIN FETCH p.menuItems mi " +
            "WHERE p.idPermissions = :id AND p.deletedAt IS NULL " +
            "AND (mi IS NULL OR mi.deletedAt IS NULL)")
    Optional<Permission> findById(Long id);
}
