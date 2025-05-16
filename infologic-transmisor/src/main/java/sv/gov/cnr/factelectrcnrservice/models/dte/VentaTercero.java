
package sv.gov.cnr.factelectrcnrservice.models.dte;

import java.io.Serializable;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Ventas por cuenta de terceros
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "nit",
    "nombre"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class VentaTercero implements Serializable
{

    /**
     * NIT por cuenta de Terceros
     * (Required)
     * 
     */
    @JsonProperty("nit")
    @JsonPropertyDescription("NIT por cuenta de Terceros")
    @Pattern(regexp = "^([0-9]{14}|[0-9]{9})$")
    @NotNull
    public String nit;
    /**
     * Nombre, denominación o razón social del Tercero
     * (Required)
     * 
     */
    @JsonProperty("nombre")
    @JsonPropertyDescription("Nombre, denominaci\u00f3n o raz\u00f3n social del Tercero")
    @Size(min = 3, max = 200)
    @NotNull
    public String nombre;
    private final static long serialVersionUID = -5770904590117305294L;

}
