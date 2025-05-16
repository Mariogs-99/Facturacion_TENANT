package sv.gov.cnr.factelectrcnrservice.models.dte.contingencia;

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
 * Motivo de la contingenia.
 *
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
        "fInicio",
        "fFin",
        "hInicio",
        "hFin",
        "tipoContingencia",
        "motivoContingencia"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Motivo {

    /**
     * Fecha inicio de la contingencia
     * (Required)
     *
     */
    @JsonProperty("fInicio")
    @JsonPropertyDescription("Fecha inicio de la contingencia")
    @NotNull
    public String fInicio;
    /**
     * Fecha de finalizacion de la contingencia
     * (Required)
     *
     */
    @JsonProperty("fFin")
    @JsonPropertyDescription("Fecha de finalizacion de la contingencia")
    @NotNull
    public String fFin;
    /**
     * Hora inicio de la contingencia.
     * (Required)
     *
     */
    @JsonProperty("hInicio")
    @JsonPropertyDescription("Hora inicio de la contingencia.")
    @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]?$")
    @NotNull
    public String hInicio;
    /**
     * Hora fin de la contingencia.
     * (Required)
     *
     */
    @JsonProperty("hFin")
    @JsonPropertyDescription("Hora fin de la contingencia.")
    @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]?$")
    @NotNull
    public String hFin;
    /**
     * Debe seleccional del Catalogo de la Contingencias cualquiera de las 5 opciones que aplique segun el motivo.
     * (Required)
     *
     */
    @JsonProperty("tipoContingencia")
    @JsonPropertyDescription("Debe seleccional del Catalogo de la Contingencias cualquiera de las 5 opciones que aplique segun el motivo.")
    @NotNull
    public Motivo.TipoContingencia tipoContingencia;
    /**
     * Descripcion del motivo de la contingencia.
     * (Required)
     *
     */
    @JsonProperty("motivoContingencia")
    @JsonPropertyDescription("Descripcion del motivo de la contingencia.")
    @Size(max = 500)
    @NotNull
    public String motivoContingencia;


    /**
     * Debe seleccional del Catalogo de la Contingencias cualquiera de las 5 opciones que aplique segun el motivo.
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
        private final static Map<Integer, Motivo.TipoContingencia> CONSTANTS = new HashMap<Integer, Motivo.TipoContingencia>();

        static {
            for (Motivo.TipoContingencia c: values()) {
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
        public static Motivo.TipoContingencia fromValue(Integer value) {
            Motivo.TipoContingencia constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }

}
