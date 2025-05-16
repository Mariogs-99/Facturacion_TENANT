package sv.gov.cnr.cnrpos.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.models.security.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolDTO {

    private Long idRole;
    private String nameRole;
    private String descriptionRole;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private List<Long> permissionIds;
    private Set<User> user;
    private Set<PermissionDTO> permission;
}
