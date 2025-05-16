package sv.gov.cnr.factelectrcnrservice.models.dte.contingencia;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;
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
        "codigoGeneracion",
        "fTransmision",
        "hTransmision"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Identificacion {

    /**
     * Version del esquema del DTE
     * (Required)
     *
     */
    @JsonProperty("version")
    @JsonPropertyDescription("Version del esquema del DTE")
    @NotNull
    private final Integer version = 3;
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
     * Debe cumplir con el estandar del UUID v4, el cual Debe ser unico por evento de contingencia (el cual no debe repetirse) longitud de 36 digitos sin guiones codigo de generacion consiste en un numero identificador inico, aleatorio y universal.
     * (Required)
     *
     */
    @JsonProperty("codigoGeneracion")
    @JsonPropertyDescription("Debe cumplir con el estandar del UUID v4, el cual Debe ser unico por evento de contingencia (el cual no debe repetirse) longitud de 36 digitos sin guiones codigo de generacion consiste en un numero identificador inico, aleatorio y universal.")
    @Pattern(regexp = "^[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}$")
    @Size(min = 36, max = 36)
    @NotNull
    public String codigoGeneracion;
    /**
     * Fecha de emision (formato yyyy-mm-dd)
     * (Required)
     *
     */
    @JsonProperty("fTransmision")
    @JsonPropertyDescription("Fecha de emision (formato yyyy-mm-dd)")
    @NotNull
    public String fTransmision;
    /**
     * Hora de emision
     * (Required)
     *
     */
    @JsonProperty("hTransmision")
    @JsonPropertyDescription("Hora de emision")
    @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]?$")
    @NotNull
    public String hTransmision;


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
