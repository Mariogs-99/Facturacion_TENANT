package sv.gov.cnr.cnrpos.entities;

import jakarta.persistence.*;
import lombok.Data;
import sv.gov.cnr.cnrpos.models.idscompuestos.MenuMenuItemsId;

import java.io.Serializable;

@Data
@Entity
@Table(name = "menu_menu_items")
@IdClass(MenuMenuItemsId.class)
public class MenuMenuItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_MENU", referencedColumnName = "ID_MENU", insertable = false, updatable = false)
    private Menu menu;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_MENU_ITEMS", referencedColumnName = "ID_MENU_ITEMS", insertable = false, updatable = false)
    private MenuItems menuItem;

    // Constructors, getters, and setters
}
