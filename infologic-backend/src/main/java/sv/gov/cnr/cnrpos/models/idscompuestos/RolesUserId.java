package sv.gov.cnr.cnrpos.models.idscompuestos;

import lombok.Data;

import java.io.Serializable;

@Data
public class RolesUserId implements Serializable {
    private Long role;
    private Long user;
}
