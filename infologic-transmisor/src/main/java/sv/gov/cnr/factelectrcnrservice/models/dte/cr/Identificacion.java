
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
    public final String tipoDte = "07";
    /**
     * Número de Control
     * (Required)
     * 
     */
    @JsonProperty("numeroControl")
    @JsonPropertyDescription("N\u00famero de Control")
    @Pattern(regexp = "^DTE-07-[A-Z0-9]{8}-[0-9]{15}$")
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
     * Tipo de Contingencia
     * (Required)
     * 
     */
    @JsonProperty("tipoContingencia")
    @JsonPropertyDescription("Tipo de Contingencia")
    @NotNull
    public Identificacion.TipoContingencia tipoContingencia;
    /**
     * Motivo de Contingencia
     * (Required)
     * 
     */
    @JsonProperty("motivoContin")
    @JsonPropertyDescription("Motivo de Contingencia")
    @NotNull
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
    private final static long serialVersionUID = -2092041049302353499L;


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


    /**
     * Tipo de Moneda
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoMoneda {

        USD("USD");
        private final String value;
        private final static Map<String, TipoMoneda> CONSTANTS = new HashMap<String, TipoMoneda>();

        static {
            for (TipoMoneda c: values()) {
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
        public static TipoMoneda fromValue(String value) {
            TipoMoneda constant = CONSTANTS.get(value);
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
    public enum TipoOperacion {

        _1(1),
        _2(2);
        private final Integer value;
        private final static Map<Integer, TipoOperacion> CONSTANTS = new HashMap<Integer, TipoOperacion>();

        static {
            for (TipoOperacion c: values()) {
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
        public static TipoOperacion fromValue(Integer value) {
            TipoOperacion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }

    /**
     * Tipo de Contigencia
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
        private final static Map<Integer, Identificacion.TipoContingencia> CONSTANTS = new HashMap<Integer, TipoContingencia>();

        static {
            for (TipoContingencia c: values()) {
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
        public static TipoContingencia fromValue(Integer value) {
            TipoContingencia constant = CONSTANTS.get(value);
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

        _1(1.0D),
        _2(2.0D);
        private final Double value;
        private final static Map<Double, TipoModelo> CONSTANTS = new HashMap<Double, TipoModelo>();

        static {
            for (TipoModelo c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoModelo(Double value) {
            this.value = value;
        }

        @JsonValue
        public Double value() {
            return this.value;
        }

        @JsonCreator
        public static TipoModelo fromValue(Double value) {
            TipoModelo constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }

}
