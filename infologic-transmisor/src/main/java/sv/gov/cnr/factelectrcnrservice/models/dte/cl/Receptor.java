
package sv.gov.cnr.factelectrcnrservice.models.dte.cl;

import java.io.Serializable;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sv.gov.cnr.factelectrcnrservice.models.dte.Direccion;


/**
 * Receptor
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @Size(min = 2, max = 8)
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
    @Size(min = 5, max = 6)
    @NotNull
    public String codActividad;
    /**
     * Actividad Económica (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("descActividad")
    @JsonPropertyDescription("Actividad Econ\u00f3mica (Receptor)")
    @Size(min = 5, max = 150)
    @NotNull
    public String descActividad;
    /**
     * Nombre Comercial (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("nombreComercial")
    @JsonPropertyDescription("Nombre Comercial (Receptor)")
    @Size(min = 5, max = 150)
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
    private final static long serialVersionUID = 3832257085708852727L;
    @AssertTrue(message = "Si departamento es 01, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento01() {
        return isValidMunicipioPattern("01", "^0[1-9]|1[0-2]$");
    }

    @AssertTrue(message = "Si departamento es 02 o 10, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento02Or10() {
        return isValidMunicipioPattern("02", "^0[1-9]|1[0-3]$") || isValidMunicipioPattern("10", "^0[1-9]|1[0-3]$");
    }

    @AssertTrue(message = "Si departamento es 03 o 07, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento03Or07() {
        return isValidMunicipioPattern("03", "^0[1-9]|1[0-6]$") || isValidMunicipioPattern("07", "^0[1-9]|1[0-6]$");
    }

    @AssertTrue(message = "Si departamento es 04, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento04() {
        return isValidMunicipioPattern("04", "^0[1-9]|[12][0-9]|3[0-3]$");
    }

    @AssertTrue(message = "Si departamento es 05 o 08, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento05Or08() {
        return isValidMunicipioPattern("05", "^0[1-9]|1[0-9]|2[0-2]$") || isValidMunicipioPattern("08", "^0[1-9]|1[0-9]|2[0-2]$");
    }

    @AssertTrue(message = "Si departamento es 06, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento06() {
        return isValidMunicipioPattern("06", "^0[1-9]|1[0-9]$");
    }

    @AssertTrue(message = "Si departamento es 09, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento09() {
        return isValidMunicipioPattern("09", "^0[1-9]$");
    }

    @AssertTrue(message = "Si departamento es 11, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento11() {
        return isValidMunicipioPattern("11", "^0[1-9]|1[0-9]|2[0-3]$");
    }

    @AssertTrue(message = "Si departamento es 12, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento12() {
        return isValidMunicipioPattern("12", "^0[1-9]|1[0-9]|20$");
    }

    @AssertTrue(message = "Si departamento es 13, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento13() {
        return isValidMunicipioPattern("13", "^0[1-9]|1[0-9]|2[0-6]$");
    }

    @AssertTrue(message = "Si departamento es 14, entonces municipio debe tener un patrón específico.")
    private boolean isValidMunicipioForDepartamento14() {
        return isValidMunicipioPattern("14", "^0[1-9]|1[0-8]$");
    }

    private boolean isValidMunicipioPattern(String departamento, String pattern) {
        return departamento.equals(this.direccion.departamento) && this.direccion.municipio != null &&
                this.direccion.municipio.matches(pattern);
    }

}
