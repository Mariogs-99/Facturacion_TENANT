package sv.gov.cnr.cnrpos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "producto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO", nullable = false)
    private Integer idProducto; // ⚠️ Cambiado de codId a idProducto

    @Column(name = "CLASIFICACION", length = 255)
    private String clasificacion;

    @Column(name = "CODIGO_PRODUCTO", length = 50, unique = true)
    private String codigo_producto;

    @Column(name = "NOMBRE", length = 255, nullable = false)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 500)
    private String descripcion;

    @Column(name = "CODIGO_INGRESO", length = 50)
    private String codigo_ingreso;

    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio; // ⚠️ Cambiado de Double a BigDecimal

    @Column(name = "IVA")
    private BigDecimal iva; // ⚠️ Cambiado de Double a BigDecimal

    @Column(name = "TIPO", length = 10)
    private String tipo;

    @Column(name = "TOTAL")
    private BigDecimal total; // ⚠️ Cambiado de Double a BigDecimal

    @Column(name = "ESTADO", length = 5)
    private String estado;

    @Column(name = "EDITABLE", length = 5)
    private String editable;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;

    @Column(name = "DELETED_AT")
    private Timestamp deletedAt;
}
