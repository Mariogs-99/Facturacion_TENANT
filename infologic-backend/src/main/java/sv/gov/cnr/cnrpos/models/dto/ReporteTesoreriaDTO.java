package sv.gov.cnr.cnrpos.models.dto;

import lombok.Data;

@Data
public class ReporteTesoreriaDTO {

    private String reporteName;
    private String formato;
    private String fechaDesde;
    private String fechaHasta;
    private Integer usuario;
    private String sucursal;
    private String mes;
    private String anio;
    private String fecha;
    private String modulo;

}
