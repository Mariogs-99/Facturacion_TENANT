
package sv.gov.cnr.factelectrcnrservice.models.dte.cl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.gov.cnr.factelectrcnrservice.models.dte.Direccion;


/**
 * Emisor
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
    "tipoEstablecimiento",
    "direccion",
    "telefono",
    "correo",
    "codEstableMH",
    "codEstable",
    "codPuntoVentaMH",
    "codPuntoVenta"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Emisor implements Serializable
{

    /**
     * NIT (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("nit")
    @JsonPropertyDescription("NIT (Emisor)")
    @Pattern(regexp = "^([0-9]{14}|[0-9]{9})$")
    @Size(max = 14)
    @NotNull
    public String nit;
    /**
     * NRC (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("nrc")
    @JsonPropertyDescription("NRC (Emisor)")
    @Pattern(regexp = "^[0-9]{1,8}$")
    @Size(min = 2, max = 8)
    @NotNull
    public String nrc;
    /**
     * Nombre, denominación o razón social del contribuyente (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("nombre")
    @JsonPropertyDescription("Nombre, denominaci\u00f3n o raz\u00f3n social del contribuyente (Emisor)")
    @Size(min = 1, max = 250)
    @NotNull
    public String nombre;
    /**
     * Código de Actividad Económica (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("codActividad")
    @JsonPropertyDescription("C\u00f3digo de Actividad Econ\u00f3mica (Emisor)")
    @Pattern(regexp = "^[0-9]{2,6}$")
    @Size(min = 5, max = 6)
    @NotNull
    public String codActividad;
    /**
     * Actividad Económica (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("descActividad")
    @JsonPropertyDescription("Actividad Econ\u00f3mica (Emisor)")
    @Size(min = 5, max = 150)
    @NotNull
    public String descActividad;
    /**
     * Nombre Comercial (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("nombreComercial")
    @JsonPropertyDescription("Nombre Comercial (Emisor)")
    @Size(max = 150)
    @NotNull
    public String nombreComercial;
    /**
     * Tipo de establecimiento (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("tipoEstablecimiento")
    @JsonPropertyDescription("Tipo de establecimiento (Emisor)")
    @NotNull
    public Emisor.TipoEstablecimiento tipoEstablecimiento;
    /**
     * Dirección (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("direccion")
    @JsonPropertyDescription("Direcci\u00f3n (Emisor)")
    @Valid
    @NotNull
    public Direccion direccion;
    /**
     * Teléfono (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("telefono")
    @JsonPropertyDescription("Tel\u00e9fono (Emisor)")
    @Size(min = 8, max = 30)
    @NotNull
    public String telefono;
    /**
     * Correo electrónico (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("correo")
    @JsonPropertyDescription("Correo electr\u00f3nico (Emisor)")
    @Size(min = 3, max = 100)
    @NotNull
    public String correo;
    /**
     * Código del establecimiento asignado por el MH
     * (Required)
     * 
     */
    @JsonProperty("codEstableMH")
    @JsonPropertyDescription("C\u00f3digo del establecimiento asignado por el MH")
    @Size(min = 4, max = 4)
    @NotNull
    public String codEstableMH;
    /**
     * Código del establecimiento asignado por el contribuyente
     * (Required)
     * 
     */
    @JsonProperty("codEstable")
    @JsonPropertyDescription("C\u00f3digo del establecimiento asignado por el contribuyente")
    @Size(min = 4, max = 4)
    @NotNull
    public String codEstable;
    /**
     * Código del Punto de Venta (Emisor) Asignado por el MH
     * (Required)
     * 
     */
    @JsonProperty("codPuntoVentaMH")
    @JsonPropertyDescription("C\u00f3digo del Punto de Venta (Emisor) Asignado por el MH")
    @Size(min = 4, max = 4)
    @NotNull
    public String codPuntoVentaMH;
    /**
     * Código del Punto de Venta (Emisor) asignado por el contribuyente
     * (Required)
     * 
     */
    @JsonProperty("codPuntoVenta")
    @JsonPropertyDescription("C\u00f3digo del Punto de Venta (Emisor) asignado por el contribuyente")
    @Size(min = 1, max = 15)
    @NotNull
    public String codPuntoVenta;
    private final static long serialVersionUID = -844208526588493782L;


    /**
     * Tipo de establecimiento (Emisor)
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoEstablecimiento {

        _01("01"),
        _02("02"),
        _04("04"),
        _07("07"),
        _20("20");
        private final String value;
        private final static Map<String, TipoEstablecimiento> CONSTANTS = new HashMap<String, TipoEstablecimiento>();

        static {
            for (TipoEstablecimiento c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoEstablecimiento(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static TipoEstablecimiento fromValue(String value) {
            TipoEstablecimiento constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }
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
