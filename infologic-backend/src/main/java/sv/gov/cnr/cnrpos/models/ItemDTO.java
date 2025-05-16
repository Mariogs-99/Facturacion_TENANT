package sv.gov.cnr.cnrpos.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDTO {
    @Valid
    @NotNull(message = "itemCNR no puede ser nulo")
    private ItemCNR itemCNR;

    @Min(value = 1, message = "La cantidad de ítems mínima es 1")
    @NotNull(message = "cantidad no puede ser nulo")
    private Integer cantidad;

    @NotNull(message = "precioUnitario no puede ser nulo")
    @Positive(message = "El precioUnitario debe ser un valor positivo")
    private Double precioUnitario;

    private BigDecimal ventaNosujeta;
    private BigDecimal ventaExenta;
    private BigDecimal ventaGravada;
    private BigDecimal ventaNogravada;
    private Double ivaItem;

    private String editable;
    private String codigoProducto;
    private String codigoIngreso;
    private String nroDocumento;

    public ItemDTO(ItemCNR itemCNR, Integer cantidad, Double precioUnitario, BigDecimal ventaNosujeta, BigDecimal ventaNogravada,
                   BigDecimal ventaExenta, BigDecimal ventaGravada, Double ivaItem, String editable, String codigoIngreso, String codigoProducto, String numeroDocumento) {
        this.itemCNR = itemCNR;
        this.cantidad = cantidad;
        this.nroDocumento = numeroDocumento;

        // Asignar un valor por defecto si el precio unitario es nulo
        this.precioUnitario = (precioUnitario != null) ? precioUnitario : 0.0; // O asigna cualquier otro valor por defecto que desees
        this.ivaItem = (ivaItem != null) ? ivaItem : 0.0;
        this.ventaNosujeta = (ventaNosujeta != null) ? ventaNosujeta : BigDecimal.valueOf(0.0);
        this.ventaExenta = (ventaExenta != null) ? ventaExenta : BigDecimal.valueOf(0.0);
        this.ventaGravada = (ventaGravada != null) ? ventaGravada : BigDecimal.valueOf(0.0);
        this.ventaNogravada = (ventaNogravada != null) ? ventaNogravada : BigDecimal.valueOf(0.0);
        this.editable = (editable != null) ? editable : "N";
        this.codigoIngreso = codigoIngreso;
        this.codigoProducto = codigoProducto;

    }


}

