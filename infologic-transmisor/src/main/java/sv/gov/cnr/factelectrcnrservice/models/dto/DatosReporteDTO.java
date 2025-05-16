package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.Data;
import sv.gov.cnr.factelectrcnrservice.models.enums.TipoReporte;

@Data
public class DatosReporteDTO {
    String fechaDesde;
    String fechaHasta;
    Boolean esCSV;
    TipoReporte tipoReporte;
}
