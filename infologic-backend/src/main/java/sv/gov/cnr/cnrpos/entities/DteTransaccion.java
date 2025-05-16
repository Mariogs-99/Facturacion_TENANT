package sv.gov.cnr.cnrpos.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "dte_transaccion")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DteTransaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DTE_TRANSACCION", nullable = false)
    Long idDteTransaccion;

    @ManyToOne
    @JoinColumn(name = "ID_TRANSACCION", referencedColumnName = "ID_TRANSACCION")
    @NotNull(message = "Una transaccion debe de ser referenciada")
    @JsonBackReference
    Transaccion transaccion;

    @Lob
    @Column(name = "DTE_JSON", columnDefinition = "TEXT")
    private String dteJson;

    @Lob
    @Column(name = "DTE_JSON_FIRMADO", columnDefinition = "TEXT")
    private String dteJsonFirmado;

    @Column(name = "VERSION_APP")
    private String versionApp;

    @Column(name = "CODIGO_GENERACION")
    private String codigoGeneracion;

    @Column(name = "CODIGO_GENERACION_ANUL")
    private String codigoGeneracionAnulacion;

    @Column(name = "CODIGO_GENERACION_CONT")
    private String codigoGeneracionContingencia;

    @Column(name = "NUMERO_DTE")
    private String numeroDte;

    @Column(name = "SELLO_RECIBIDO_MH")
    private String selloRecibidoMh;

    @Column(name = "SELLO_RECIBIDO_MH_CONT")
    private String selloRecibidoMhCont;

    @Column(name = "SELLO_RECIBIDO_MH_ANUL")
    private String selloRecibidoMhAnul;

    @Column(name = "FECHA_GENERACION")
    private String fechaGeneracion;

    @Column(name = "FECHA_PROCESADO")
    private String fechaProcesado;

    @Column(name = "FECHA_ANULADO")
    private String fechaAnulacion;

    @Column(name = "HORA_ANULA")
    private String horaAnula;

    @Column(name = "ESTADO_DTE")
    private String estadoDte;

    @Column(name = "AMBIENTE")
    private String ambiente;

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "TIPO_DTE")
    private String tipoDTE;

    @Column(name = "FECHA_EMISION")
    private String fechaEmision;

    @Column(name = "HORA_EMISION")
    private String horaEmision;

    @Lob
    @Column(name = "OBSERVACIONES", columnDefinition = "TEXT")
    private String observaciones;
}
