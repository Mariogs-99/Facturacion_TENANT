package sv.gov.cnr.factelectrcnrservice.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cuerpo_doc_cr")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuerpoDocCR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRANSACCION", nullable = false)
    @JsonBackReference
    private Transaccion transaccion;

    @Column(name = "NUMERO_ITEM")
    private Long numeroItem;

    @Column(name = "TIPO_DTE", length = 2, nullable = false)
    private String tipoDte;

    @Column(name = "TIPO_DOC", nullable = false)
    private Integer tipoDoc;

    @Column(name = "NUM_DOCUMENTO", length = 250, nullable = false)
    private String numDocumento;

    @Column(name = "FECHA_EMISION", length = 10, nullable = false)
    private String fechaEmision;

    @Column(name = "MONTO_SUJETO", precision = 10, scale = 2, nullable = false)
    private BigDecimal montoSujeto;

    @Column(name = "IVA_RETENIDO", precision = 10, scale = 2, nullable = false)
    private BigDecimal ivaRetenido;

    @Column(name = "DESCRIPCION", length = 250, nullable = false)
    private String descripcion;

    @Column(name = "TIPO_RETENCION")
    private String tipoRetencion;

}
