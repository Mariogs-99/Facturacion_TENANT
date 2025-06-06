package sv.gov.cnr.cnrpos.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "documento_asociado")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoAsociado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documento_asociado_seq")
    @SequenceGenerator(name = "documento_asociado_seq", sequenceName = "DOCUMENTO_ASOCIADO_SEQ", allocationSize = 1)
    @Column(name = "ID_DOCUMENTO_ASOCIADO", nullable = false)
    private Long id;

    @Column(name = "TIPO_DTE")
    private String tipoDTE;

    @Column(name = "SERIE_DOCUMENTO")
    private String serieDocumento;

    @Column(name = "NRO_PRE_IMPRESO")
    private Integer nroPreimpreso;

    @Column(name = "TIPO_GENERACION")
    private Integer tipoGeneracion;

    @Column(name = "CODIGO_GENERACION")
    private String codigoGeneracion;

    @Column(name = "FECHA_EMISION")
    private String fechaEmision;

    @Column(name =  "MONTO_DOCUMENTO")
    private BigDecimal montoDocumento;

    @ManyToOne
    @JoinColumn(name = "ID_TRANSACCION")
    @JsonBackReference
    private Transaccion transaccion;

    @ManyToOne
    @JoinColumn(name = "ID_TRANSACCION_HIJA")
    @JsonBackReference
    private Transaccion transaccionHija;

}
