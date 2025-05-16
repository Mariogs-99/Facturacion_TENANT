
package sv.gov.cnr.factelectrcnrservice.models.dte;

import java.io.Serializable;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Dirección (Emisor)
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "departamento",
    "municipio",
    "complemento"
})
@Generated("jsonschema2pojo")
@Data
public class Direccion implements Serializable
{

    /**
     * Dirección Departamento
     * (Required)
     * 
     */
    @JsonProperty("departamento")
    @JsonPropertyDescription("Direcci\u00f3n Departamento")
    @Pattern(regexp = "^0[1-9]|1[0-4]$")
    public String departamento;
    /**
     * Dirección Municipio
     * (Required)
     * 
     */
    @JsonProperty("municipio")
    @JsonPropertyDescription("Direcci\u00f3n Municipio")
    @Pattern(regexp = "^[0-9]{2}$")
    public String municipio;
    /**
     * Dirección complemento
     * (Required)
     * 
     */
    @JsonProperty("complemento")
    @JsonPropertyDescription("Direcci\u00f3n complemento")
    @Size(min = 1, max = 200)
    public String complemento;
    private final static long serialVersionUID = -1895905187831146981L;
}
