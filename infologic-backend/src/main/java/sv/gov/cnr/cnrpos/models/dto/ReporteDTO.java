package sv.gov.cnr.cnrpos.models.dto;

import lombok.Data;

@Data
public class ReporteDTO {

    private String reporteName;
    private String formato;
    private String fechaDesde;
    private String fechaHasta;
}
