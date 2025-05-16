
package sv.gov.cnr.factelectrcnrservice.models.dte.ccf;

import java.io.Serializable;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "codDocAsociado",
    "descDocumento",
    "detalleDocumento",
    "medico"
})
@Generated("jsonschema2pojo")
public class OtrosDocumento implements Serializable
{

    /**
     * Documento asociado
     * (Required)
     * 
     */
    @JsonProperty("codDocAsociado")
    @JsonPropertyDescription("Documento asociado")
    @DecimalMin("1")
    @DecimalMax("4")
    @NotNull
    public Integer codDocAsociado;
    /**
     * Identificación del documento asociado
     * (Required)
     * 
     */
    @JsonProperty("descDocumento")
    @JsonPropertyDescription("Identificaci\u00f3n del documento asociado")
    @Size(max = 100)
    @NotNull
    public String descDocumento;
    /**
     * Descripción de documento asociado
     * (Required)
     * 
     */
    @JsonProperty("detalleDocumento")
    @JsonPropertyDescription("Descripci\u00f3n de documento asociado")
    @Size(max = 300)
    @NotNull
    public String detalleDocumento;
    /**
     * Médico
     * (Required)
     * 
     */
    @JsonProperty("medico")
    @JsonPropertyDescription("M\u00e9dico")
    @NotNull
    public Medico medico;
    private final static long serialVersionUID = -4744772057728749149L;

}
