
package sv.gov.cnr.factelectrcnrservice.models.dte.ccf;

import java.io.Serializable;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "codigo",
    "montoPago",
    "referencia",
    "plazo",
    "periodo"
})
@Generated("jsonschema2pojo")
public class Pago implements Serializable
{

    /**
     * Código de forma de pago
     * (Required)
     * 
     */
    @JsonProperty("codigo")
    @JsonPropertyDescription("C\u00f3digo de forma de pago")
    @Pattern(regexp = "^(0[1-9]||1[0-4]||99)$")
    @Size(max = 2)
    @NotNull
    public String codigo;
    /**
     * Monto por forma de pago
     * (Required)
     * 
     */
    @JsonProperty("montoPago")
    @JsonPropertyDescription("Monto por forma de pago")
    @NotNull
    public Double montoPago;
    /**
     * Referencia de modalidad de pago
     * (Required)
     * 
     */
    @JsonProperty("referencia")
    @JsonPropertyDescription("Referencia de modalidad de pago")
    @Size(max = 50)
    @NotNull
    public String referencia;
    /**
     * Plazo
     * (Required)
     * 
     */
    @JsonProperty("plazo")
    @JsonPropertyDescription("Plazo")
    @Pattern(regexp = "^0[1-3]$")
    @NotNull
    public String plazo;
    /**
     * Período de plazo
     * (Required)
     * 
     */
    @JsonProperty("periodo")
    @JsonPropertyDescription("Per\u00edodo de plazo")
    @NotNull
    public Double periodo;
    private final static long serialVersionUID = 7090530644511732864L;

}
