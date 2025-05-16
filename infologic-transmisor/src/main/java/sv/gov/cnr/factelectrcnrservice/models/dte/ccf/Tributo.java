
package sv.gov.cnr.factelectrcnrservice.models.dte.ccf;

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

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "codigo",
    "descripcion",
    "valor"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Tributo implements Serializable
{

    /**
     * Resumen Código de Tributo
     * (Required)
     * 
     */
    @JsonProperty("codigo")
    @JsonPropertyDescription("Resumen C\u00f3digo de Tributo")
    @Size(min = 2, max = 2)
    @NotNull
    public String codigo;
    /**
     * Nombre del Tributo
     * (Required)
     * 
     */
    @JsonProperty("descripcion")
    @JsonPropertyDescription("Nombre del Tributo")
    @Size(min = 2, max = 150)
    @NotNull
    public String descripcion;
    /**
     * Valor del Tributo
     * (Required)
     * 
     */
    @JsonProperty("valor")
    @JsonPropertyDescription("Valor del Tributo")
    @NotNull
    public Double valor;
    private final static long serialVersionUID = 9109646168575031460L;

}
