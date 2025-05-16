package sv.gov.cnr.cnrpos.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sv.gov.cnr.cnrpos.models.enums.TipoItem;

@Data
public class ItemCNR {
    @NotNull
    private TipoItem tipoItem;//Ligado a CAT-011 Tipo de ítem
    @NotEmpty
    private String nombreItem;
    private Double precioUnitario;//puede ser nulo
    private String descripcion;//puede ser nulo

}
