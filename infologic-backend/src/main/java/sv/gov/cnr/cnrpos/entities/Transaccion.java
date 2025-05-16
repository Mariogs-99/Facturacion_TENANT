package sv.gov.cnr.cnrpos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sv.gov.cnr.cnrpos.models.enums.EstatusCola;
import sv.gov.cnr.cnrpos.models.security.User;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "transaccion")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaccion implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TRANSACCION", nullable = false)
    private Long idTransaccion;

    @ManyToOne
    @JoinColumn(name = "ID_SUCURSAL")
    private Sucursal sucursal;

    @ManyToOne
    @JoinColumn(name = "ID_USER")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    @Column(name = "FORMA_DE_PAGO")
    private String formaDePago;//ligado a CAT-017 Forma de Pago

    @Column(name = "TIPO_DTE")
    private String tipoDTE;//ligado a CAT-002 Tipo de Documento

    @Column(name = "CONDICION_PAGO")
    private Integer condicionOperacion;//ligado a CAT-016 Condición de la Operación

    @Column(name = "NOTAS", length = 500)
    private String notas;

    @Column(name = "STATUS")
    private EstatusCola status;//viene de la cola de procesamiento (pendiente, en proceso, procesado, error)


    @Column(name = "TOTAL_SIN_DESCUENTO")
    private BigDecimal totalSinDescuento;

    @Column(name = "PORCENTAJE_DESCUENTO")
    private Double porcentajeDescuento = 0.0;//porcentaje de cuanto se desconto

    @Column(name = "MONTO_DESCUENTO", precision = 10, scale = 2)
    private BigDecimal montoDescuento;//monto en dolares de cuanto se desconto

    @Column(name = "DESCRIPCION_DESCUENTO")
    private String descripcionDescuento;//informacion del tipo de descuento que tiene el cliente por ser gobierno, alcaldia, etc

    @Column(name = "TOTAL_TRANSACCION", precision = 18, scale = 2)
    private BigDecimal totalTransaccion;

    @Column(name = "TOTAL_EXENTO")
    private BigDecimal totalExento;

    @Column(name = "TOTAL_GRAVADO")
    private BigDecimal totalGravado;

    @Column(name = "TOTAL_NOGRAVADO")
    private BigDecimal totalNogravado;

    @Column(name = "TOTAL_NOSUJETO")
    private BigDecimal totalNosujeto;

    @Column(name = "TOTAL_IVA")
    private BigDecimal totalIva;

    @Column(name = "DESCUENTO_NOSUJETO")
    private BigDecimal descuentoNosujeto;

    @Column(name = "DESCUENTO_GRAVADO")
    private BigDecimal descuentoGravado;

    @Column(name = "DESCUENTO_EXENTO")
    private BigDecimal descuentoExento;

    @Column(name = "IVA_RETENIDO")
    private BigDecimal ivaRetenido;

    @Column(name = "IVA_PERCIVIDO")
    private BigDecimal ivaPercivido;

    @Column(name = "RENTA_RETENIDO")
    private BigDecimal rentaRetenido;

    @Column(name = "SALDO_AFAVOR")
    private BigDecimal saldoAfavor;

    @Column(name = "FECHA_TRANSACCION")
    private Date fechaTransaccion;//para reportar al MH

    @Column(name = "HORA_TRANSACCION")
    private Timestamp horaTransaccion;//para reportar al MH

    @Column(name = "NUMERO_DTE", length = 200)
    private String numeroDTE;

    @Column(name = "CODIGO_GENERACION")
    private String codigoGeneracion;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<Item> items;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<DocumentoAsociado> documentoAsociados;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<ComprobantePago> comprobantePagos;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<CuerpoDocCR> cuerpoDocCR;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DteTransaccion> dteTransacciones;

    private String estadoDte;
}