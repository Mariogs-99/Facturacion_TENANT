
package sv.gov.cnr.factelectrcnrservice.models.dte.cr;

import java.io.Serializable;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Resumen
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "totalSujetoRetencion",
    "totalIVAretenido",
    "totalIVAretenidoLetras"
})
@Data
@NoArgsConstructor
@Generated("jsonschema2pojo")
public class Resumen implements Serializable
{

    /**
     * Total Monto Sujeto a retención
     * (Required)
     * 
     */
    @JsonProperty("totalSujetoRetencion")
    @JsonPropertyDescription("Total Monto Sujeto a retenci\u00f3n")
    @NotNull
    public Double totalSujetoRetencion;
    /**
     * Total IVA Retenido
     * (Required)
     * 
     */
    @JsonProperty("totalIVAretenido")
    @JsonPropertyDescription("Total IVA Retenido")
    @NotNull
    public Double totalIVAretenido;
    /**
     * Valor en letras
     * (Required)
     * 
     */
    @JsonProperty("totalIVAretenidoLetras")
    @JsonPropertyDescription("Valor en letras")
    @Size(max = 200)
    @NotNull
    public String totalIVAretenidoLetras;
    private final static long serialVersionUID = 8287266184394907756L;

}
