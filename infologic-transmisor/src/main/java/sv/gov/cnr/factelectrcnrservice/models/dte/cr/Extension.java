
package sv.gov.cnr.factelectrcnrservice.models.dte.cr;

import java.io.Serializable;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


/**
 * Extensión
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nombEntrega",
    "docuEntrega",
    "nombRecibe",
    "docuRecibe",
    "observaciones"
})
@Generated("jsonschema2pojo")
public class Extension implements Serializable
{

    /**
     * Nombre del responsable que Genera el DTE
     * (Required)
     * 
     */
    @JsonProperty("nombEntrega")
    @JsonPropertyDescription("Nombre del responsable que Genera el DTE")
    @Size(min = 5, max = 100)
    @NotNull
    public String nombEntrega;
    /**
     * Documento de identificación de quien genera el DTE
     * (Required)
     * 
     */
    @JsonProperty("docuEntrega")
    @JsonPropertyDescription("Documento de identificaci\u00f3n de quien genera el DTE")
    @Size(min = 5, max = 25)
    @NotNull
    public String docuEntrega;
    /**
     * Nombre del responsable de la operación por parte del receptor
     * (Required)
     * 
     */
    @JsonProperty("nombRecibe")
    @JsonPropertyDescription("Nombre del responsable de la operaci\u00f3n por parte del receptor")
    @Size(min = 5, max = 100)
    @NotNull
    public String nombRecibe;
    /**
     * Documento de identificación del responsable de la operación por parte del receptor
     * (Required)
     * 
     */
    @JsonProperty("docuRecibe")
    @JsonPropertyDescription("Documento de identificaci\u00f3n del responsable de la operaci\u00f3n por parte del receptor")
    @Size(min = 5, max = 25)
    @NotNull
    public String docuRecibe;
    /**
     * Observaciones
     * (Required)
     * 
     */
    @JsonProperty("observaciones")
    @JsonPropertyDescription("Observaciones")
    @Size(max = 3000)
    @NotNull
    public String observaciones;
    private final static long serialVersionUID = -6310078223137004254L;

}
