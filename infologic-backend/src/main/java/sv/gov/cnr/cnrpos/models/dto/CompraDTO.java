package sv.gov.cnr.cnrpos.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sv.gov.cnr.cnrpos.entities.Sucursal;

import java.sql.Timestamp;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompraDTO {
    private Long idCompra;
    private String codigoGeneracion;
    private String numeroControl;
    private String fechaEmision;
    private Long tipoOperacion;
    private Long tipoDocumento;
    private String selloRecepcion;
    private Long idProveedor;
    private String totalGravado;
    private String totalExento;
    private String totalOperacion;
    private UserDTO user;
    private Sucursal sucursal;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
