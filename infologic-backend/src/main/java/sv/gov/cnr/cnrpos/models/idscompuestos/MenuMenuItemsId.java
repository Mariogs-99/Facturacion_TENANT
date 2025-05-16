package sv.gov.cnr.cnrpos.models.idscompuestos;

import lombok.Data;

import java.io.Serializable;

@Data
public class MenuMenuItemsId implements Serializable {
    private Long menu;
    private Long menuItem;

}
