package sv.gov.cnr.factelectrcnrservice.models.security;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERMISSIONS", nullable = false)
    private Long idPermissions;

    @Column(name = "NAME_PERMISSIONS", length = 150)
    private String namePermissions;

    @Column(name = "DESCRIPTION_PERMISSIONS", length = 250)
    private String descriptionPermissions;

    @Column(name = "IMAGE_PERMISSIONS", length = 250)
    private String imagePermissions;

    @Column(name = "METHOD_PERMISSIONS", length = 255)
    private String methodPermissions;

    @Column(name = "URI_PERMISSIONS", length = 255)
    private String uriPermissions;

    @Column(name = "SLUG_PERMISSIONS", length = 400)
    private String slugPermissions;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;

    @ManyToMany
    private Set<Rol> roles;


    @Transient
    private List<Long> menuItemsIds;


}