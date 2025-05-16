
package sv.gov.cnr.factelectrcnrservice.models.dte.cl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "numItem",
    "tipoDte",
    "tipoGeneracion",
    "numeroDocumento",
    "fechaGeneracion",
    "ventaNoSuj",
    "ventaExenta",
    "ventaGravada",
    "exportaciones",
    "tributos",
    "ivaItem",
    "obsItem"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class CuerpoDocumento implements Serializable
{

    /**
     * N° de ítem
     * (Required)
     * 
     */
    @JsonProperty("numItem")
    @JsonPropertyDescription("N\u00b0 de \u00edtem")
    @DecimalMin("1")
    @DecimalMax("500")
    @NotNull
    public Integer numItem;
    /**
     * Tipo de ítem
     * (Required)
     * 
     */
    @JsonProperty("tipoDte")
    @JsonPropertyDescription("Tipo de \u00edtem")
    @NotNull
    public CuerpoDocumento.TipoDte tipoDte;
    /**
     * Tipo de generación del documento
     * (Required)
     * 
     */
    @JsonProperty("tipoGeneracion")
    @JsonPropertyDescription("Tipo de generaci\u00f3n del documento")
    @NotNull
    public CuerpoDocumento.TipoGeneracion tipoGeneracion;
    /**
     * Número de documento relacionado
     * (Required)
     * 
     */
    @JsonProperty("numeroDocumento")
    @JsonPropertyDescription("N\u00famero de documento relacionado")
    @Size(min = 1, max = 36)
    @NotNull
    public String numeroDocumento;
    /**
     * Fecha de generación del  relacionado
     * (Required)
     * 
     */
    @JsonProperty("fechaGeneracion")
    @JsonPropertyDescription("Fecha de generaci\u00f3n del  relacionado")
    @NotNull
    public String fechaGeneracion;
    /**
     * Ventas no Sujetas
     * (Required)
     * 
     */
    @JsonProperty("ventaNoSuj")
    @JsonPropertyDescription("Ventas no Sujetas")
    @NotNull
    public Double ventaNoSuj;
    /**
     * Ventas Exentas
     * (Required)
     * 
     */
    @JsonProperty("ventaExenta")
    @JsonPropertyDescription("Ventas Exentas")
    @NotNull
    public Double ventaExenta;
    /**
     * Ventas Gravadas
     * (Required)
     * 
     */
    @JsonProperty("ventaGravada")
    @JsonPropertyDescription("Ventas Gravadas")
    @NotNull
    public Double ventaGravada;
    /**
     * Ventas Gravadas
     * (Required)
     * 
     */
    @JsonProperty("exportaciones")
    @JsonPropertyDescription("Ventas Gravadas")
    @NotNull
    public Double exportaciones;
    /**
     * Tributo sujeto a cálculo de IVA
     * (Required)
     * 
     */
    @JsonProperty("tributos")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("Tributo sujeto a c\u00e1lculo de IVA")
    @NotNull
    public Set<Tributo> tributos;
    /**
     * IVA por ítem
     * (Required)
     * 
     */
    @JsonProperty("ivaItem")
    @JsonPropertyDescription("IVA por \u00edtem")
    @NotNull
    public Double ivaItem;
    /**
     * Observaciones por ítem
     * (Required)
     * 
     */
    @JsonProperty("obsItem")
    @JsonPropertyDescription("Observaciones por \u00edtem")
    @Size(min = 3, max = 3000)
    @NotNull
    public String obsItem;
    private final static long serialVersionUID = -2952231339112475235L;


    /**
     * Tipo de ítem
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoDte {

        _01("01"),
        _03("03"),
        _05("05"),
        _06("06"),
        _11("11");
        private final String value;
        private final static Map<String, TipoDte> CONSTANTS = new HashMap<String, TipoDte>();

        static {
            for (TipoDte c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoDte(String value) {
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
        public static TipoDte fromValue(String value) {
            TipoDte constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Tipo de generación del documento
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoGeneracion {

        _1(1),
        _2(2);
        private final Integer value;
        private final static Map<Integer, TipoGeneracion> CONSTANTS = new HashMap<Integer, TipoGeneracion>();

        static {
            for (TipoGeneracion c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoGeneracion(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static TipoGeneracion fromValue(Integer value) {
            TipoGeneracion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }
    @AssertTrue(message = "Validación específica para tipoGeneracion igual a 1: númeroDocumento debe seguir un patrón específico.")
    private boolean isValidTipoGeneracion1NumeroDocumentoPattern() {
        return tipoGeneracion != TipoGeneracion._1 || (numeroDocumento != null && numeroDocumento.matches("^[A-Z0-9]{1,20}$"));
    }

    @AssertTrue(message = "Validación específica para tipoGeneracion distinto de 1: númeroDocumento debe seguir otro patrón específico.")
    private boolean isValidTipoGeneracionNot1NumeroDocumentoPattern() {
        return tipoGeneracion == TipoGeneracion._1 || (numeroDocumento != null && numeroDocumento.matches("^[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}$"));
    }

}
