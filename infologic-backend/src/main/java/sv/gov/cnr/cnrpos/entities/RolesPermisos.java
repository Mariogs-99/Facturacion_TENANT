package sv.gov.cnr.cnrpos.entities;
import jakarta.persistence.*;
import lombok.Data;
import sv.gov.cnr.cnrpos.models.idscompuestos.RolesPermisosId;
import sv.gov.cnr.cnrpos.models.security.Rol;
import sv.gov.cnr.cnrpos.models.security.Permission;

import java.io.Serializable;

@Data
@Entity
@Table(name = "roles_permisos")
@IdClass(RolesPermisosId.class)
public class RolesPermisos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_PERMISSIONS", referencedColumnName = "ID_PERMISSIONS", insertable = false, updatable = false)
    private Permission permission;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID_ROLE", insertable = false, updatable = false)
    private Rol role;

    // Constructors, getters, and setters
}
