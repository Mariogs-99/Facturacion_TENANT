package sv.gov.cnr.cnrpos.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {
    private Integer idProducto; // ⚠️ Asegurado que coincida con Producto (idProducto)
    private String clasificacion;
    private String codigo_producto;
    private String nombre;
    private String descripcion;
    private String codigo_ingreso;
    private BigDecimal precio;
    private BigDecimal iva;
    private String tipo;
    private BigDecimal total;
    private String estado;
    private String editable;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
