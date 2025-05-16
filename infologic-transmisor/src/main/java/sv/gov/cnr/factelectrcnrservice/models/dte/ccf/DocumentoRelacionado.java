
package sv.gov.cnr.factelectrcnrservice.models.dte.ccf;

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
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "tipoDocumento",
    "tipoGeneracion",
    "numeroDocumento",
    "fechaEmision"
})
@Generated("jsonschema2pojo")
public class DocumentoRelacionado implements Serializable
{

    /**
     * Tipo de Documento Tributario Relacionado
     * (Required)
     * 
     */
    @JsonProperty("tipoDocumento")
    @JsonPropertyDescription("Tipo de Documento Tributario Relacionado")
    @NotNull
    public DocumentoRelacionado.TipoDocumento tipoDocumento;
    /**
     * Tipo de Generación del Documento Tributario relacionado
     * (Required)
     * 
     */
    @JsonProperty("tipoGeneracion")
    @JsonPropertyDescription("Tipo de Generaci\u00f3n del Documento Tributario relacionado")
    @NotNull
    public DocumentoRelacionado.TipoGeneracion tipoGeneracion;
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
     * Fecha de Generación del Documento Relacionado
     * (Required)
     * 
     */
    @JsonProperty("fechaEmision")
    @JsonPropertyDescription("Fecha de Generaci\u00f3n del Documento Relacionado")
    @NotNull
    public String fechaEmision;
    private final static long serialVersionUID = 970331733160469457L;


    /**
     * Tipo de Documento Tributario Relacionado
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoDocumento {

        _04("04"),
        _08("08"),
        _09("09");
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


    /**
     * Tipo de Generación del Documento Tributario relacionado
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

}
