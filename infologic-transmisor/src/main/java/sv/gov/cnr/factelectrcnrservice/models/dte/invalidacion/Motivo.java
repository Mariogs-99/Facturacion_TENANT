
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
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Datos del motivo de Invalidacion
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "tipoAnulacion",
    "motivoAnulacion",
    "nombreResponsable",
    "tipDocResponsable",
    "numDocResponsable",
    "nombreSolicita",
    "tipDocSolicita",
    "numDocSolicita"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Motivo {

    /**
     * Tipo de invalidacion
     * (Required)
     * 
     */
    @JsonProperty("tipoAnulacion")
    @JsonPropertyDescription("Tipo de invalidacion")
    @NotNull
    public Integer tipoAnulacion;
    /**
     * Motivo de invalidacion
     * (Required)
     * 
     */
    @JsonProperty("motivoAnulacion")
    @JsonPropertyDescription("Motivo de invalidacion")
    @Size(min = 5, max = 250)
    @NotNull
    public String motivoAnulacion;
    /**
     * Nombre de la persona responsable de invalidar el DTE
     * (Required)
     * 
     */
    @JsonProperty("nombreResponsable")
    @JsonPropertyDescription("Nombre de la persona responsable de invalidar el DTE")
    @Size(min = 5, max = 100)
    @NotNull
    public String nombreResponsable;
    /**
     * Tipo documento de identificación CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO
     * (Required)
     * 
     */
    @JsonProperty("tipDocResponsable")
    @JsonPropertyDescription("Tipo documento de identificaci\u00f3n CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO")
    @NotNull
    public String tipDocResponsable;
    /**
     * Número de documento de identificación 
     * (Required)
     * 
     */
    @JsonProperty("numDocResponsable")
    @JsonPropertyDescription("N\u00famero de documento de identificaci\u00f3n ")
    @Size(min = 3, max = 20)
    @NotNull
    public String numDocResponsable;
    /**
     * Nombre de la persona que solicita invalidar el DTE
     * (Required)
     * 
     */
    @JsonProperty("nombreSolicita")
    @JsonPropertyDescription("Nombre de la persona que solicita invalidar el DTE")
    @Size(min = 5, max = 100)
    @NotNull
    public String nombreSolicita;
    /**
     * Tipo documento de identificación solicitante CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO
     * (Required)
     * 
     */
    @JsonProperty("tipDocSolicita")
    @JsonPropertyDescription("Tipo documento de identificaci\u00f3n solicitante CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO")
    @NotNull
    public String tipDocSolicita;
    /**
     * Número de documento de identificación solicitante.
     * (Required)
     * 
     */
    @JsonProperty("numDocSolicita")
    @JsonPropertyDescription("N\u00famero de documento de identificaci\u00f3n solicitante.")
    @Size(min = 3, max = 20)
    @NotNull
    public String numDocSolicita;


    /**
     * Tipo documento de identificación CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipDocResponsable {

        _36("36"),
        _13("13"),
        _02("02"),
        _03("03"),
        _37("37");
        private final String value;
        private final static Map<String, Motivo.TipDocResponsable> CONSTANTS = new HashMap<String, Motivo.TipDocResponsable>();

        static {
            for (Motivo.TipDocResponsable c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipDocResponsable(String value) {
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
        public static Motivo.TipDocResponsable fromValue(String value) {
            Motivo.TipDocResponsable constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Tipo documento de identificación solicitante CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipDocSolicita {

        _36("36"),
        _13("13"),
        _02("02"),
        _03("03"),
        _37("37");
        private final String value;
        private final static Map<String, Motivo.TipDocSolicita> CONSTANTS = new HashMap<String, Motivo.TipDocSolicita>();

        static {
            for (Motivo.TipDocSolicita c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipDocSolicita(String value) {
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
        public static Motivo.TipDocSolicita fromValue(String value) {
            Motivo.TipDocSolicita constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Tipo de invalidacion
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoAnulacion {

        _1(1, "01"),
        _2(2, "02"),
        _3(3, "03");

        private final Integer number;  // Stores the number
        private final String value;    // Stores the formatted string value
        private static final Map<String, TipoAnulacion> CONSTANTS = new HashMap<>();

        // Static initializer to populate the CONSTANTS map
        static {
            for (TipoAnulacion c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        // Constructor that initializes both fields, number and value
        TipoAnulacion(Integer number, String value) {
            this.number = number;
            this.value = value;
        }

        // @JsonValue annotates the method to use when serializing the object
        @JsonValue
        public String getValue() {
            return this.value;
        }

        // @JsonCreator defines the factory method for deserializing an object from a JSON key
        @JsonCreator
        public static TipoAnulacion fromValue(String value) {
            TipoAnulacion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException("Unknown value: " + value);
            }
            return constant;
        }

    }

}
