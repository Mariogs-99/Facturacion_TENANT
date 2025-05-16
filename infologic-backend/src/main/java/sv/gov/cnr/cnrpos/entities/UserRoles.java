package sv.gov.cnr.cnrpos.entities;
import jakarta.persistence.*;
import lombok.Data;
import sv.gov.cnr.cnrpos.models.idscompuestos.RolesUserId;
import sv.gov.cnr.cnrpos.models.security.Rol;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.models.security.Rol;


import java.io.Serializable;

@Data
@Entity
@Table(name = "users_roles")
@IdClass(RolesUserId.class)
public class UserRoles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID_ROLE")
    private Rol role;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    private User user;

    // Constructors, getters, and setters
}
