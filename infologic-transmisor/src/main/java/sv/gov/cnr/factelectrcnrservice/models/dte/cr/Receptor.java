
package sv.gov.cnr.factelectrcnrservice.models.dte.cr;

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
    "tipoDocumento",
    "numDocumento",
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
     * Tipo de documento de identificación (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("tipoDocumento")
    @JsonPropertyDescription("Tipo de documento de identificaci\u00f3n (Receptor)")
    @NotNull
    public Receptor.TipoDocumento tipoDocumento;
    /**
     * Número de documento de Identificación (Receptor)
     * (Required)
     * 
     */
    @JsonProperty("numDocumento")
    @JsonPropertyDescription("N\u00famero de documento de Identificaci\u00f3n (Receptor)")
    @Size(min = 3, max = 20)
    @NotNull
    public String numDocumento;
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
    @Pattern(regexp = "^[0-9]{5,6}$")
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
    @Pattern(regexp = "^[0-9+;]{8,30}$")
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
    private final static long serialVersionUID = -2676615574068482629L;


    /**
     * Tipo de documento de identificación (Receptor)
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoDocumento {

        _36("36"),
        _13("13"),
        _37("37"),
        _03("03"),
        _02("02");
        private final String value;
        private final static Map<String, TipoDocumento> CONSTANTS = new HashMap<String, TipoDocumento>();

        static {
            for (TipoDocumento c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoDocumento(String value) {
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
        public static TipoDocumento fromValue(String value) {
            TipoDocumento constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
