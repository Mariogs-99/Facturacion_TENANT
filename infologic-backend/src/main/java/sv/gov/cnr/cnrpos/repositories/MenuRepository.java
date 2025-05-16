package sv.gov.cnr.cnrpos.repositories;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.models.security.Permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Override
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.menuItems mi WHERE m.deletedAt IS NULL AND (mi IS NULL OR mi.deletedAt IS NULL)")
    List<Menu> findAll();

    @Override
    @Query(value = "SELECT m FROM Menu m LEFT JOIN FETCH m.menuItems mi WHERE m.deletedAt IS NULL AND (mi IS NULL OR mi.deletedAt IS NULL)",
            countQuery = "SELECT COUNT(m) FROM Menu m WHERE m.deletedAt IS NULL")
    Page<Menu> findAll(Pageable page);

    @Override
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.menuItems mi WHERE m.idMenu = :id AND m.deletedAt IS NULL AND (mi IS NULL OR mi.deletedAt IS NULL)")
    Optional<Menu> findById(Long id);

    @Query("SELECT DISTINCT m FROM Menu m JOIN m.menuItems mi " +
            "WHERE mi.idMenuItems IN (SELECT pmi.menuItems.idMenuItems FROM PermisosMenuItems pmi " +
            "WHERE pmi.permission.idPermissions IN (SELECT rp.permission.idPermissions FROM RolesPermisos rp " +
            "WHERE rp.role.idRole IN (SELECT ur.role.idRole FROM UserRoles ur WHERE ur.user.idUser = :userId))) " +
            "AND (mi.deletedAt IS NULL OR mi IS NULL)")
    Set<Menu> findMenusByUserId(@Param("userId") Long userId);

    @Query("SELECT pmi.menuItems FROM PermisosMenuItems pmi " +
            "WHERE pmi.permission.idPermissions IN (SELECT rp.permission.idPermissions FROM RolesPermisos rp " +
            "WHERE rp.role.idRole IN (SELECT ur.role.idRole FROM UserRoles ur WHERE ur.user.idUser = :userId)) " +
            "AND pmi.menuItems.deletedAt IS NULL")
    Set<MenuItems> findMenuItemsByUserId(@Param("userId") Long userId);

    @Query("SELECT mi.menus FROM MenuItems mi WHERE mi.idMenuItems IN " +
            "(SELECT pmi.menuItems.idMenuItems FROM PermisosMenuItems pmi " +
            "WHERE pmi.permission.idPermissions IN (SELECT rp.permission.idPermissions FROM RolesPermisos rp " +
            "WHERE rp.role.idRole IN (SELECT ur.role.idRole FROM UserRoles ur WHERE ur.user.idUser = :userId))) " +
            "AND (mi IS NULL OR mi.deletedAt IS NULL)")
    List<Object[]> findMenuAndItemsByUserId(@Param("userId") Long userId);

    @Query("SELECT r.idRole FROM User u JOIN u.roles r WHERE u.idUser = :userId")
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT rp.permission.idPermissions FROM RolesPermisos rp " +
            "WHERE rp.role.idRole IN (SELECT ur.role.idRole FROM UserRoles ur WHERE ur.user.idUser = :userId)")
    List<Long> findPermissionIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT pmi.menuItems.idMenuItems FROM PermisosMenuItems pmi " +
            "WHERE pmi.permission.idPermissions IN (SELECT DISTINCT rp.permission.idPermissions " +
            "FROM RolesPermisos rp WHERE rp.role.idRole IN " +
            "(SELECT ur.role.idRole FROM UserRoles ur WHERE ur.user.idUser = :userId))")
    List<Long> findMenuItemsIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT mmip.menu, mmip.menuItem FROM MenuMenuItems mmip " +
            "WHERE mmip.menuItem.idMenuItems IN (SELECT pmi.menuItems.idMenuItems FROM PermisosMenuItems pmi " +
            "WHERE pmi.permission.idPermissions IN ( SELECT rp.permission.idPermissions " +
            "FROM RolesPermisos rp WHERE rp.role.idRole IN " +
            "(SELECT ur.role.idRole FROM UserRoles ur WHERE ur.user.idUser = :userId))) " +
            "GROUP BY mmip.menu, mmip.menuItem")
    List<Object[]> findMenuMenuItemsIdsByUserId(@Param("userId") Long userId);
}
