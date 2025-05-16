
package sv.gov.cnr.factelectrcnrservice.models.dte.fex;

import java.io.Serializable;
import java.util.Set;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "numItem",
    "cantidad",
    "codigo",
    "uniMedida",
    "descripcion",
    "precioUni",
    "montoDescu",
    "ventaGravada",
    "tributos",
    "noGravado"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class CuerpoDocumento implements Serializable
{

    /**
     * N° de ítem
     * (Required)
     * 
     */
    @JsonProperty("numItem")
    @JsonPropertyDescription("N\u00b0 de \u00edtem")
    @DecimalMin("1")
    @DecimalMax("2000")
    @NotNull
    public Integer numItem;
    /**
     * Cantidad
     * (Required)
     * 
     */
    @JsonProperty("cantidad")
    @JsonPropertyDescription("Cantidad")
    @NotNull
    public Double cantidad;
    /**
     * Código
     * (Required)
     * 
     */
    @JsonProperty("codigo")
    @JsonPropertyDescription("C\u00f3digo")
    @Size(min = 1, max = 200)
    @NotNull
    public String codigo;
    /**
     * Unidad de Medida
     * (Required)
     * 
     */
    @JsonProperty("uniMedida")
    @JsonPropertyDescription("Unidad de Medida")
    @DecimalMin("1")
    @DecimalMax("99")
    @NotNull
    public Integer uniMedida;
    /**
     * Descripción
     * (Required)
     * 
     */
    @JsonProperty("descripcion")
    @JsonPropertyDescription("Descripci\u00f3n")
    @Size(max = 1000)
    @NotNull
    public String descripcion;
    /**
     * Precio Unitario
     * (Required)
     * 
     */
    @JsonProperty("precioUni")
    @JsonPropertyDescription("Precio Unitario")
    @NotNull
    public Double precioUni;
    /**
     * Descuento, Bonificación, Rebajas por ítem
     * (Required)
     * 
     */
    @JsonProperty("montoDescu")
    @JsonPropertyDescription("Descuento, Bonificaci\u00f3n, Rebajas por \u00edtem")
    @NotNull
    public Double montoDescu;
    /**
     * Ventas Gravadas
     * (Required)
     * 
     */
    @JsonProperty("ventaGravada")
    @JsonPropertyDescription("Ventas Gravadas")
    @NotNull
    public Double ventaGravada;
    /**
     * Código del Tributo
     * (Required)
     * 
     */
    @JsonProperty("tributos")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("C\u00f3digo del Tributo")
    @Size(min = 1)
    @NotNull
    public Set<String> tributos;
    /**
     * Cargos / Abonos que no afectan la base imponible
     * (Required)
     * 
     */
    @JsonProperty("noGravado")
    @JsonPropertyDescription("Cargos / Abonos que no afectan la base imponible")
    @NotNull
    public Double noGravado;
    private final static long serialVersionUID = 3534662428846994124L;

}
