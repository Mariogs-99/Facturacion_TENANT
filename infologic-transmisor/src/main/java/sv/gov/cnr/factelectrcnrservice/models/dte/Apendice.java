
package sv.gov.cnr.factelectrcnrservice.models.dte;

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
    "campo",
    "etiqueta",
    "valor"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Apendice implements Serializable
{

    /**
     * Nombre del campo
     * (Required)
     * 
     */
    @JsonProperty("campo")
    @JsonPropertyDescription("Nombre del campo")
    @Size(min = 2, max = 25)
    @NotNull
    public String campo;
    /**
     * Descripción
     * (Required)
     * 
     */
    @JsonProperty("etiqueta")
    @JsonPropertyDescription("Descripci\u00f3n")
    @Size(min = 3, max = 50)
    @NotNull
    public String etiqueta;
    /**
     * Valor/Dato
     * (Required)
     * 
     */
    @JsonProperty("valor")
    @JsonPropertyDescription("Valor/Dato")
    @Size(min = 1, max = 150)
    @NotNull
    public String valor;
    private final static long serialVersionUID = -5150361461212675454L;

}
