
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private final static long serialVersionUID = -2820244510838744022L;


    /**
     * Tipo de Documento Tributario Relacionado
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoDocumento {

        _04("04"),
        _09("09");
        private final String value;
        private final static Map<String, DocumentoRelacionado.TipoDocumento> CONSTANTS = new HashMap<String, DocumentoRelacionado.TipoDocumento>();

        static {
            for (DocumentoRelacionado.TipoDocumento c: values()) {
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
        public static DocumentoRelacionado.TipoDocumento fromValue(String value) {
            DocumentoRelacionado.TipoDocumento constant = CONSTANTS.get(value);
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
        private final static Map<Integer, DocumentoRelacionado.TipoGeneracion> CONSTANTS = new HashMap<Integer, DocumentoRelacionado.TipoGeneracion>();

        static {
            for (DocumentoRelacionado.TipoGeneracion c: values()) {
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
        public static DocumentoRelacionado.TipoGeneracion fromValue(Integer value) {
            DocumentoRelacionado.TipoGeneracion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }

}
