package sv.gov.cnr.cnrpos.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.MenuItems;

import java.util.List;
import java.util.Set;

@Repository
public interface MenuItemsRepository extends JpaRepository<MenuItems, Long> {

    @Override
    @Query("SELECT m FROM MenuItems m WHERE m.deletedAt IS NULL")
    List<MenuItems> findAll();

    @Override
    @Query(value = "SELECT m FROM MenuItems m WHERE m.deletedAt IS NULL",
            countQuery = "SELECT COUNT(m) FROM MenuItems m WHERE m.deletedAt IS NULL")
    Page<MenuItems> findAll(Pageable page);

    @Query(value = "SELECT m FROM MenuItems m WHERE m.deletedAt IS NULL AND m.idMenu = :id",
            countQuery = "SELECT COUNT(m) FROM MenuItems m WHERE m.deletedAt IS NULL AND m.idMenu = :id")
    Page<MenuItems> findAllByMenuId(@Param("id") Long id, Pageable page);

    @Query("SELECT m FROM MenuItems m WHERE m.deletedAt IS NULL and m.idMenu = :idMenu")
    Set<MenuItems> findByMenuId(@Param("idMenu") Long idMenu);
}
