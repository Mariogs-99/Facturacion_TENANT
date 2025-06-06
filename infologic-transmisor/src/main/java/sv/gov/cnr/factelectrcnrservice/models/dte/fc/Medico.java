
package sv.gov.cnr.factelectrcnrservice.models.dte.fc;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * Médico
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nombre",
    "nit",
    "docIdentificacion",
    "tipoServicio"
})
@Generated("jsonschema2pojo")
public class Medico implements Serializable
{

    /**
     * Nombre de médico que presta el Servicio
     * (Required)
     * 
     */
    @JsonProperty("nombre")
    @JsonPropertyDescription("Nombre de m\u00e9dico que presta el Servicio")
    @Size(max = 100)
    @NotNull
    public String nombre;
    /**
     * NIT de médico que presta el Servicio
     * (Required)
     * 
     */
    @JsonProperty("nit")
    @JsonPropertyDescription("NIT de m\u00e9dico que presta el Servicio")
    @Pattern(regexp = "^([0-9]{14}|[0-9]{9})$")
    @NotNull
    public String nit;
    /**
     * Documento de identificación de médico no domiciliados
     * (Required)
     * 
     */
    @JsonProperty("docIdentificacion")
    @JsonPropertyDescription("Documento de identificaci\u00f3n de m\u00e9dico no domiciliados")
    @Size(min = 2, max = 25)
    @NotNull
    public String docIdentificacion;
    /**
     * Código del Servicio realizado
     * (Required)
     * 
     */
    @JsonProperty("tipoServicio")
    @JsonPropertyDescription("C\u00f3digo del Servicio realizado")
    @NotNull
    public Double tipoServicio;
    private final static long serialVersionUID = 9213162637329453108L;

}
