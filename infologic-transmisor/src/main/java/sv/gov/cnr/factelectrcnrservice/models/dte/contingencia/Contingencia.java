package sv.gov.cnr.factelectrcnrservice.models.dte.contingencia;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Reporte de contingencia
 * <p>
 * The root schema comprises the entire JSON document.
 *
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
        "identificacion",
        "emisor",
        "detalleDTE",
        "motivo"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Contingencia {

    /**
     * An explanation about the purpose of this instance.
     * (Required)
     *
     */
    @JsonProperty("identificacion")
    @JsonPropertyDescription("An explanation about the purpose of this instance.")
    @Valid
    @NotNull
    public Identificacion identificacion;
    /**
     * Informacion del emisor.
     * (Required)
     *
     */
    @JsonProperty("emisor")
    @JsonPropertyDescription("Informacion del emisor.")
    @Valid
    @NotNull
    public Emisor emisor;
    /**
     * Detalle de los documentos que se enviaran en contingencia.
     * (Required)
     *
     */
    @JsonProperty("detalleDTE")
    @JsonPropertyDescription("Detalle de los documentos que se enviaran en contingencia.")
    @Size(min = 1, max = 1000)
    @Valid
    @NotNull
    public List<DetalleDTE> detalleDTE;
    /**
     * Motivo de la contingenia.
     * (Required)
     *
     */
    @JsonProperty("motivo")
    @JsonPropertyDescription("Motivo de la contingenia.")
    @Valid
    @NotNull
    public Motivo motivo;

}
