package sv.gov.cnr.cnrpos.entities;
import jakarta.persistence.*;
import lombok.Data;
import sv.gov.cnr.cnrpos.models.idscompuestos.PermisosMenuItemsId;
import sv.gov.cnr.cnrpos.models.security.Permission;

import java.io.Serializable;

@Data
@Entity
@Table(name = "permisos_menu_items")
@IdClass(PermisosMenuItemsId.class)
public class PermisosMenuItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_MENU_ITEMS", referencedColumnName = "ID_MENU_ITEMS", insertable = false, updatable = false)
    private MenuItems menuItems;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_PERMISSIONS", referencedColumnName = "ID_PERMISSIONS", insertable = false, updatable = false)
    private Permission permission;

    // Constructors, getters, and setters
}
