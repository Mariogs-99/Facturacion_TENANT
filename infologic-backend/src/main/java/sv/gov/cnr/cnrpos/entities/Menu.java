package sv.gov.cnr.cnrpos.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MENU", nullable = false)
    private Long idMenu;

    @NotNull
    @Column(name = "ID_CATEGORY")
    private Long idCategory;

    @NotNull
    @Column(name = "NAME_MENU", length = 150)
    private String nameMenu;

    @Nullable
    @Column(name = "DESCRIPTION_MENU", length = 200)
    private String descriptionMenu;

    @NotNull
    @Column(name = "SLUG_MENU", length = 400)
    private String slugMenu;

    @Nullable
    @Column(name = "IMAGE_MENU", length = 250)
    private String imageMenu;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;

    @Transient
    private Boolean createItemsMenu;

    @Transient
    private String apiName;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ID_CATEGORY", referencedColumnName = "ID_CATALOGO", insertable = false, updatable = false)
    private RCatalogos category;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "menu_menu_items", joinColumns = @JoinColumn(name = "ID_MENU", referencedColumnName = "ID_MENU"), inverseJoinColumns = @JoinColumn(name = "ID_MENU_ITEMS", referencedColumnName = "ID_MENU_ITEMS"))
    private Set<MenuItems> menuItems;

    // Constructors, getters, and setters
}

