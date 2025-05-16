package sv.gov.cnr.cnrpos.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sv.gov.cnr.cnrpos.models.security.Rol;
import java.sql.Timestamp;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionDTO {
    private Long idPermissions;
    private String namePermissions;
    private String descriptionPermissions;
    private String imagePermissions;
    private String methodPermissions;
    private String uriPermissions;
    private String slugPermissions;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private Set<MenuItemsDTO> menuItems;
    private Set<Rol> roles;
}
