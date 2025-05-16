package sv.gov.cnr.cnrpos.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PuntoVentaDTO {

    private String sucursal;
    private List<PuntoVenta> puntoVentas;


    @NoArgsConstructor
    @Getter
    @Setter
    public static class PuntoVenta {
        private long idSucursal;
        private String puntoVenta;
    }
}
