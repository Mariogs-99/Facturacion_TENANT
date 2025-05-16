
package sv.gov.cnr.factelectrcnrservice.models.dte.invalidacion;

import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Invalidacion de Documento Tributario Electronico
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "identificacion",
    "emisor",
    "documento",
    "motivo"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Invalidacion {

    /**
     * Informacion de identificacion de Invalidacion
     * (Required)
     * 
     */
    @JsonProperty("identificacion")
    @JsonPropertyDescription("Informacion de identificacion de Invalidacion")
    @Valid
    @NotNull
    public Identificacion identificacion;
    /**
     * Datos del emisor
     * (Required)
     * 
     */
    @JsonProperty("emisor")
    @JsonPropertyDescription("Datos del emisor")
    @Valid
    @NotNull
    public Emisor emisor;
    /**
     * Datos del documento a Invalidar
     * (Required)
     * 
     */
    @JsonProperty("documento")
    @JsonPropertyDescription("Datos del documento a Invalidar")
    @Valid
    @NotNull
    public Documento documento;
    /**
     * Datos del motivo de Invalidacion
     * (Required)
     * 
     */
    @JsonProperty("motivo")
    @JsonPropertyDescription("Datos del motivo de Invalidacion")
    @Valid
    @NotNull
    public Motivo motivo;

}
