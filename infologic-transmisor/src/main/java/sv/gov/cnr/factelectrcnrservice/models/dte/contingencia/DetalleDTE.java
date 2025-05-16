package sv.gov.cnr.factelectrcnrservice.models.dte.contingencia;



import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
        "noItem",
        "codigoGeneracion",
        "tipoDoc"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class DetalleDTE {

    /**
     * Numero correlativo.
     * (Required)
     *
     */
    @JsonProperty("noItem")
    @JsonPropertyDescription("Numero correlativo.")
    @NotNull
    public Integer noItem;
    /**
     * Codigo de generacion del documento reportado.
     * (Required)
     *
     */
    @JsonProperty("codigoGeneracion")
    @JsonPropertyDescription("Codigo de generacion del documento reportado.")
    @Pattern(regexp = "^[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}$")
    @NotNull
    public String codigoGeneracion;
    /**
     * Tipo de Documento Tributario Electronico
     * (Required)
     *
     */
    @JsonProperty("tipoDoc")
    @JsonPropertyDescription("Tipo de Documento Tributario Electronico")
    @Pattern(regexp = "^0[1-9]|1[0-5]$")
    @NotNull
    public String tipoDoc;

}
