package sv.gov.cnr.cnrpos.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sv.gov.cnr.cnrpos.models.security.Permission;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@Table(name = "menu_items")
@AllArgsConstructor // Generates a constructor with all properties
@NoArgsConstructor // Generates a no-args constructor
public class MenuItems implements Serializable {

    public MenuItems(Long idMenu, String nameMenuItems, String uriMenuItems, String descriptionMenuItems, String imageMenuItems, String type) {
        this.idMenu = idMenu;
        this.nameMenuItems = nameMenuItems;
        this.uriMenuItems = uriMenuItems;
        this.descriptionMenuItems = descriptionMenuItems;
        this.imageMenuItems = imageMenuItems;
        this.type = type;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MENU_ITEMS", nullable = false)
    private Long idMenuItems;

    @NotNull
    @Column(name = "ID_MENU")
    private Long idMenu;

    @NotNull
    @Column(name = "NAME_MENU_ITEMS", length = 150)
    private String nameMenuItems;

    @NotNull
    @Column(name = "URI_MENU_ITEMS", length = 255)
    private String uriMenuItems;

    @Column(name = "DESCRIPTION_MENU_ITEMS", length = 250)
    private String descriptionMenuItems;

    @Column(name = "IMAGE_MENU_ITEMS", length = 250)
    private String imageMenuItems;

    @NotNull
    @Column(name = "TYPE", length = 10)
    private String type;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;


    @ManyToMany
    private Set<Permission> permissions;

    @ManyToMany
    private Set<Menu> menus;

    // Constructors, getters, and setters
}