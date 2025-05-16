
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Identificación
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "version",
    "ambiente",
    "tipoDte",
    "numeroControl",
    "codigoGeneracion",
    "tipoModelo",
    "tipoOperacion",
    "fecEmi",
    "horEmi",
    "tipoMoneda"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Identificacion implements Serializable
{

    /**
     * Versión
     * (Required)
     * 
     */
    @JsonProperty("version")
    @JsonPropertyDescription("Versi\u00f3n")
    @NotNull
    public final Integer version = 1;
    /**
     * Ambiente de destino
     * (Required)
     * 
     */
    @JsonProperty("ambiente")
    @JsonPropertyDescription("Ambiente de destino")
    @NotNull
    public Identificacion.Ambiente ambiente;
    /**
     * Tipo de Documento
     * (Required)
     * 
     */
    @JsonProperty("tipoDte")
    @JsonPropertyDescription("Tipo de Documento")
    @NotNull
    public final String tipoDte = "08";
    /**
     * Número de Control
     * (Required)
     * 
     */
    @JsonProperty("numeroControl")
    @JsonPropertyDescription("N\u00famero de Control")
    @Pattern(regexp = "^DTE-08-[A-Z0-9]{8}-[0-9]{15}$")
    @Size(min = 31, max = 31)
    @NotNull
    public String numeroControl;
    /**
     * Código de Generación
     * (Required)
     * 
     */
    @JsonProperty("codigoGeneracion")
    @JsonPropertyDescription("C\u00f3digo de Generaci\u00f3n")
    @Pattern(regexp = "^[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}$")
    @Size(min = 36, max = 36)
    @NotNull
    public String codigoGeneracion;
    /**
     * Modelo de Facturación
     * (Required)
     * 
     */
    @JsonProperty("tipoModelo")
    @JsonPropertyDescription("Modelo de Facturaci\u00f3n")
    @NotNull
    public final Integer tipoModelo = 1;
    /**
     * Tipo de Transmisión
     * (Required)
     * 
     */
    @JsonProperty("tipoOperacion")
    @JsonPropertyDescription("Tipo de Transmisi\u00f3n")
    @NotNull
    public final Integer tipoOperacion = 1;
    /**
     * Fecha de Generación
     * (Required)
     * 
     */
    @JsonProperty("fecEmi")
    @JsonPropertyDescription("Fecha de Generaci\u00f3n")
    @NotNull
    public String fecEmi;
    /**
     * Hora de Generación
     * (Required)
     * 
     */
    @JsonProperty("horEmi")
    @JsonPropertyDescription("Hora de Generaci\u00f3n")
    @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]?$")
    @NotNull
    public String horEmi;
    /**
     * Tipo de Moneda
     * (Required)
     * 
     */
    @JsonProperty("tipoMoneda")
    @JsonPropertyDescription("Tipo de Moneda")
    @NotNull
    public final String tipoMoneda = "USD";
    private final static long serialVersionUID = 8317036723635430228L;


    /**
     * Ambiente de destino
     * 
     */
    @Generated("jsonschema2pojo")
    public enum Ambiente {

        _00("00"),
        _01("01");
        private final String value;
        private final static Map<String, Ambiente> CONSTANTS = new HashMap<String, Ambiente>();

        static {
            for (Ambiente c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Ambiente(String value) {
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
        public static Ambiente fromValue(String value) {
            Ambiente constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
