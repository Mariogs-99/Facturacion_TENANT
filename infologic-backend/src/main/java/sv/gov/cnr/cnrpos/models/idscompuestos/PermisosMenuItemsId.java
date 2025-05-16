package sv.gov.cnr.cnrpos.models.idscompuestos;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermisosMenuItemsId implements Serializable {
    private Long menuItems;
    private Long permission;

}
