
package sv.gov.cnr.factelectrcnrservice.models.dte.invalidacion;

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
 * Informacion de identificacion de Invalidacion
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "version",
    "ambiente",
    "codigoGeneracion",
    "fecAnula",
    "horAnula"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Identificacion {

    /**
     * Version del esquema
     * (Required)
     * 
     */
    @JsonProperty("version")
    @JsonPropertyDescription("Version del esquema")
    @NotNull
    public final Integer version = 2;
    /**
     * Ambiente de destino: 00 - Pruebas, 01 - Produccion
     * (Required)
     * 
     */
    @JsonProperty("ambiente")
    @JsonPropertyDescription("Ambiente de destino: 00 - Pruebas, 01 - Produccion")
    @NotNull
    public Identificacion.Ambiente ambiente;
    /**
     * Codigo Generacion
     * (Required)
     * 
     */
    @JsonProperty("codigoGeneracion")
    @JsonPropertyDescription("Codigo Generacion")
    @Pattern(regexp = "^[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}$")
    @Size(min = 36, max = 36)
    @NotNull
    public String codigoGeneracion;
    /**
     * Fecha de invalidacion (formato yyyy-mm-dd)
     * (Required)
     * 
     */
    @JsonProperty("fecAnula")
    @JsonPropertyDescription("Fecha de invalidacion (formato yyyy-mm-dd)")
    @NotNull
    public String fecAnula;
    /**
     * Hora de invalidacion
     * (Required)
     * 
     */
    @JsonProperty("horAnula")
    @JsonPropertyDescription("Hora de invalidacion")
    @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]?$")
    @NotNull
    public String horAnula;


    /**
     * Ambiente de destino: 00 - Pruebas, 01 - Produccion
     * 
     */
    @Generated("jsonschema2pojo")
    public enum Ambiente {

        _00("00"),
        _01("01");
        private final String value;
        private final static Map<String, Identificacion.Ambiente> CONSTANTS = new HashMap<String, Identificacion.Ambiente>();

        static {
            for (Identificacion.Ambiente c: values()) {
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
        public static Identificacion.Ambiente fromValue(String value) {
            Identificacion.Ambiente constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
