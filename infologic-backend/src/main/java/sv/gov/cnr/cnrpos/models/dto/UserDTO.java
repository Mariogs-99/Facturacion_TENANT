package sv.gov.cnr.cnrpos.models.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long idUser;
    private String firstname;
    private String lastname;
    private String carnet;
    private String usuario;
    private String tipo;
    private String email;
    private Long docType;
    private String docNumber;
    private String password;
    private String phone;
    private Boolean isActive;
    private Boolean testMode;
    private Long idCompany;
    private Long idBranch;
    private Long idPosition;
    private List<Long> rolIds;
    private Set<RolDTO> roles;
    private Set<Long> getRoless;

    private Map<String, Object> mapProperties;
}
