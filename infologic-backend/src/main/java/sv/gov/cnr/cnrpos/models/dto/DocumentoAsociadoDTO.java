package sv.gov.cnr.cnrpos.models.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DocumentoAsociadoDTO {

    String tipoDte;
    String codigoGeneracion;
    Integer tipoGeneracion;
    String serieDocumento;
    Integer nroPreimpreso;
    String fechaEmision;
    BigDecimal monto;
    Long idTransaccionHija;

}
