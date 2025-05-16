
package sv.gov.cnr.factelectrcnrservice.models.dte.nd;

import java.io.Serializable;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.gov.cnr.factelectrcnrservice.models.dte.Direccion;


/**
 * Receptor
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "nit",
    "nrc",
    "nombre",
    "codActividad",
    "descActividad",
    "nombreComercial",
    "direccion",
    "telefono",
    "correo"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Receptor implements Serializable
{

    /**
     * NIT (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("nit")
    @JsonPropertyDescription("NIT (Receptor)")
    @Pattern(regexp = "^([0-9]{14}|[0-9]{9})$")
    @NotNull
    public String nit;
    /**
     * NRC (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("nrc")
    @JsonPropertyDescription("NRC (Receptor)")
    @Pattern(regexp = "^[0-9]{1,8}$")
    @NotNull
    public String nrc;
    /**
     * Nombre, denominación o razón social del contribuyente (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("nombre")
    @JsonPropertyDescription("Nombre, denominaci\u00f3n o raz\u00f3n social del contribuyente (Receptor)")
    @Size(min = 1, max = 250)
    @NotNull
    public String nombre;
    /**
     * Código de Actividad Económica (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("codActividad")
    @JsonPropertyDescription("C\u00f3digo de Actividad Econ\u00f3mica (Receptor)")
    @Pattern(regexp = "^[0-9]{2,6}$")
    @NotNull
    public String codActividad;
    /**
     * Actividad Económica (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("descActividad")
    @JsonPropertyDescription("Actividad Econ\u00f3mica (Receptor)")
    @Size(min = 1, max = 150)
    @NotNull
    public String descActividad;
    /**
     * Nombre Comercial (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("nombreComercial")
    @JsonPropertyDescription("Nombre Comercial (Receptor)")
    @Size(min = 1, max = 150)
    @NotNull
    public String nombreComercial;
    /**
     * Dirección (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("direccion")
    @JsonPropertyDescription("Direcci\u00f3n (Receptor)")
    @Valid
    @NotNull
    public Direccion direccion;
    /**
     * Teléfono (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("telefono")
    @JsonPropertyDescription("Tel\u00e9fono (Receptor)")
    @Size(min = 8, max = 30)
    @NotNull
    public String telefono;
    /**
     * Correo electrónico (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("correo")
    @JsonPropertyDescription("Correo electr\u00f3nico (Receptor)")
    @Size(max = 100)
    @NotNull
    public String correo;
    private final static long serialVersionUID = 1710753611367946095L;

}
