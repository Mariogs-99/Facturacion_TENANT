package sv.gov.cnr.cnrpos.repositories.security;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.models.security.Rol;
import sv.gov.cnr.cnrpos.models.security.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String Email);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.email= :email")
    List<User> findByEmailOverrided(@Param("email") String Email);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.usuario= :usuario")
    List<User> findByUsuarioOverrided(@Param("usuario") String Usuario);

    Optional<User> findByUsuario(String Usuario);

    @Query("SELECT u  FROM User u WHERE u.deletedAt IS NULL AND u.carnet = :carnet  AND u.usuario = :usuario ORDER BY u.createdAt DESC")
        //@Query(value = "SELECT u.* FROM  CNR_POS.cnrpos_users u WHERE u.DELETED_AT IS NULL AND u.CARNET = :carnet AND u.usuario = :usuario ORDER BY u.created_at DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<User> loginSissuc(@Param("carnet") String carnet, @Param("usuario") String usuario);

    @Override
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    List<User> findAll();

    @Override
    @Query(value = "SELECT u FROM User u WHERE u.deletedAt IS NULL", countQuery = "SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL")
    Page<User> findAll(Pageable page);


}
