package sv.gov.cnr.cnrpos.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sv.gov.cnr.cnrpos.models.security.User;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "compra")
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compra_seq")
    @SequenceGenerator(name = "compra_seq", sequenceName = "COMPRA_SEQ", allocationSize = 1)
    @Column(name = "ID_COMPRA", nullable = false)
    private Long idCompra;

    @Nullable
    @Column(name = "CODIGO_GENERACION")
    private String codigoGeneracion;

    @Nullable
    @Column(name = "NUMERO_CONTROL")
    private String numeroControl;

    @Nullable
    @Column(name = "FECHA_EMISION")
    private String fechaEmision;

    @Nullable
    @Column(name = "TIPO_OPERACION")
    private Long tipoOperacion;

    @Nullable
    @Column(name = "TIPO_DOCUMENTO")
    private Long tipoDocumento;

    @Nullable
    @Column(name = "SELLO_RECEPCION")
    private String selloRecepcion;

    @Nullable
    @Column(name = "ID_PROVEEDOR")
    private Long idProveedor;

    @Nullable
    @Column(name = "TOTAL_GRAVADO")
    private String totalGravado;

    @Nullable
    @Column(name = "TOTAL_EXENTO")
    private String totalExento;

    @Nullable
    @Column(name = "TOTAL_OPERACION")
    private String totalOperacion;

    @ManyToOne
    @JoinColumn(name = "ID_USER")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ID_SUCURSAL")
    private Sucursal sucursal;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;
}
