package sv.gov.cnr.cnrpos.models.idscompuestos;

import lombok.Data;

import java.io.Serializable;

@Data
public class RolesPermisosId implements Serializable {
    private Long permission;
    private Long role;
}
