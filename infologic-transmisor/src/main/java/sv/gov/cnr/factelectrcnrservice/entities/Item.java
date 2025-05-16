package sv.gov.cnr.factelectrcnrservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ITEM")
    private Long idItem;

    @Column(name = "TIPO_ITEM")
    private Integer tipoItem;//amarrado a CAT-011 Tipo de ítem

    @Column(name = "NRO_DOCUMENTO")
    private String nroDocumento;

    @Column(name = "CODIGO_PRODUCTO")
    private String codigoProducto;

    @Column(name = "CLASIFICACION")
    private String clasificacion;

    @Column(name = "CODIGO_INGRESO")
    private String codigoIngreso;

    @Column(name = "EDITABLE")
    private String editable;

    @Column(name = "UNIDAD_MEDIDA", nullable = false)
    private Integer unidadMedida = 59;//Amarrado a CAT-014 Unidad de Medida. Por el momento el unico usado es UNIDAD

    @Column(name = "NOMBRE_ITEM", length = 150)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 250)
    private String descripcion;

    @Column(name = "TOTAL", precision = 10, scale = 2)
    private BigDecimal total;//calculado: (precioUnitario * cantidad)

    @Column(name = "CANTIDAD")
    private Integer cantidad;

    @Column(name = "PRECIO_UNITARIO", precision = 10,scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "IVA_ITEM", precision = 10,scale = 2)
    private BigDecimal ivaItem;

    @Column(name = "MONTO_DESCUENTO", precision = 10,scale = 2)
    private BigDecimal montoDescuento;

    @Column(name = "VTA_NOSUJETA")
    private BigDecimal ventaNosujeta;

    @Column(name = "VTA_EXENTA")
    private BigDecimal ventaExenta;

    @Column(name = "VTA_GRAVADA")
    private BigDecimal ventaGravada;

    @Column(name = "VTA_NOGRAVADA")
    private BigDecimal ventaNogravada;

    @ManyToOne
    @JoinColumn(name = "ID_TRANSACCION")
    @JsonBackReference
    private Transaccion transaccion;

}
