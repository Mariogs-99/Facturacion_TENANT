
package sv.gov.cnr.factelectrcnrservice.models.dte.fc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
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
    "tipoContingencia",
    "motivoContin",
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
    public final String tipoDte = "01";
    /**
     * Número de Control
     * (Required)
     * 
     */
    @JsonProperty("numeroControl")
    @JsonPropertyDescription("N\u00famero de Control")
    @Pattern(regexp = "^DTE-01-[A-Z0-9]{8}-[0-9]{15}$")
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
    public Identificacion.TipoModelo tipoModelo;
    /**
     * Tipo de Transmisión
     * (Required)
     * 
     */
    @JsonProperty("tipoOperacion")
    @JsonPropertyDescription("Tipo de Transmisi\u00f3n")
    @NotNull
    public Identificacion.TipoOperacion tipoOperacion;
    /**
     * Tipo de Contingencia
     * (Required)
     * 
     */
    @JsonProperty("tipoContingencia")
    @JsonPropertyDescription("Tipo de Contingencia")
    public Identificacion.TipoContingencia tipoContingencia;
    /**
     * Motivo de Contingencia
     * (Required)
     * 
     */
    @JsonProperty("motivoContin")
    @JsonPropertyDescription("Motivo de Contingencia")
    @Size(min = 5, max = 150)
    public String motivoContin;
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
    private final static long serialVersionUID = -878055482187964121L;


    /**
     * Ambiente de destino
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


    /**
     * Tipo de Contingencia
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoContingencia {

        _1(1),
        _2(2),
        _3(3),
        _4(4),
        _5(5);
        private final Integer value;
        private final static Map<Integer, Identificacion.TipoContingencia> CONSTANTS = new HashMap<Integer, Identificacion.TipoContingencia>();

        static {
            for (Identificacion.TipoContingencia c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoContingencia(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static Identificacion.TipoContingencia fromValue(Integer value) {
            Identificacion.TipoContingencia constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }


    /**
     * Modelo de Facturación
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoModelo {

        PREVIO(1),
        DIFERIDO(2);
        private final Integer value;
        private final static Map<Integer, Identificacion.TipoModelo> CONSTANTS = new HashMap<Integer, Identificacion.TipoModelo>();

        static {
            for (Identificacion.TipoModelo c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoModelo(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static Identificacion.TipoModelo fromValue(Double value) {
            Identificacion.TipoModelo constant = CONSTANTS.get(value.intValue());
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }


    /**
     * Tipo de Moneda
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoMoneda {

        USD("USD");
        private final String value;
        private final static Map<String, Identificacion.TipoMoneda> CONSTANTS = new HashMap<String, Identificacion.TipoMoneda>();

        static {
            for (Identificacion.TipoMoneda c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoMoneda(String value) {
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
        public static Identificacion.TipoMoneda fromValue(String value) {
            Identificacion.TipoMoneda constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Tipo de Transmisión
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoOperacion {//el mismo tipo de transmisión

        _1(1),
        _2(2);
        private final Integer value;
        private final static Map<Integer, Identificacion.TipoOperacion> CONSTANTS = new HashMap<Integer, Identificacion.TipoOperacion>();

        static {
            for (Identificacion.TipoOperacion c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoOperacion(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static Identificacion.TipoOperacion fromValue(Integer value) {
            Identificacion.TipoOperacion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }
    @AssertTrue(message = "Si tipoOperacion es 1, entonces tipoModelo debe ser 1, tipoContingencia y motivoContin deben ser nulos")
    private boolean isTipoOperacion1Valid() {
        return tipoOperacion != TipoOperacion._1 || (tipoModelo == TipoModelo.PREVIO && tipoContingencia == null && motivoContin == null);
    }

    @AssertTrue(message = "Si tipoOperacion es 2, entonces tipoContingencia debe ser un número entero")
    private boolean isTipoOperacion2Valid() {
        return tipoOperacion != TipoOperacion._2 || (tipoContingencia != null && (tipoContingencia == TipoContingencia._1 || tipoContingencia == TipoContingencia._2));
    }

    @AssertTrue(message = "Si tipoContingencia es 5, entonces motivoContin debe ser una cadena de texto")
    private boolean isTipoContingencia5Valid() {
        return tipoContingencia != TipoContingencia._5 || motivoContin != null;
    }

}
