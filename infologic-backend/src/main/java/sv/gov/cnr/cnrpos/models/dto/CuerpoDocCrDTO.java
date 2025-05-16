package sv.gov.cnr.cnrpos.models.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import sv.gov.cnr.cnrpos.entities.Transaccion;

import java.math.BigDecimal;

@Data
public class CuerpoDocCrDTO {

    private Long numeroItem;
    private String tipoDte;
    private Integer tipoDoc;
    private String numDocumento;
    private String fechaEmision;
    private BigDecimal montoSujeto;
    private BigDecimal ivaRetenido;
    private String descripcion;
    private String tipoRetencion;

}
