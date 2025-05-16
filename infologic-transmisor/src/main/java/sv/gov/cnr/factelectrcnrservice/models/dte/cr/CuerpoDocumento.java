
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
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "numItem",
    "tipoDte",
    "tipoDoc",
    "numDocumento",
    "fechaEmision",
    "montoSujetoGrav",
    "codigoRetencionMH",
    "ivaRetenido",
    "descripcion"
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
     * Tipo de Documento Tributario Relacionado
     * (Required)
     * 
     */
    @JsonProperty("tipoDte")
    @JsonPropertyDescription("Tipo de Documento Tributario Relacionado")
    @NotNull
    public CuerpoDocumento.TipoDte tipoDte;
    /**
     * Tipo de generación del documento
     * (Required)
     * 
     */
    @JsonProperty("tipoDoc")
    @JsonPropertyDescription("Tipo de generaci\u00f3n del documento")
    @NotNull
    public CuerpoDocumento.TipoDoc tipoDoc;
    /**
     * Número de documento relacionado
     * (Required)
     * 
     */
    @JsonProperty("numDocumento")
    @JsonPropertyDescription("N\u00famero de documento relacionado")
    @NotNull
    public String numDocumento;
    /**
     * Fecha de generación del  relacionado
     * (Required)
     * 
     */
    @JsonProperty("fechaEmision")
    @JsonPropertyDescription("Fecha de generaci\u00f3n del  relacionado")
    @NotNull
    public String fechaEmision;
    /**
     * Monto Sujeto a retención
     * (Required)
     * 
     */
    @JsonProperty("montoSujetoGrav")
    @JsonPropertyDescription("Monto Sujeto a retenci\u00f3n")
    @NotNull
    public Double montoSujetoGrav;
    /**
     * Código retención MH
     * (Required)
     * 
     */
    @JsonProperty("codigoRetencionMH")
    @JsonPropertyDescription("C\u00f3digo retenci\u00f3n MH")
    @NotNull
    public CuerpoDocumento.CodigoRetencionMH codigoRetencionMH;
    /**
     * IVA Retenido
     * (Required)
     * 
     */
    @JsonProperty("ivaRetenido")
    @JsonPropertyDescription("IVA Retenido")
    @NotNull
    public Double ivaRetenido;
    /**
     * Descripción
     * (Required)
     * 
     */
    @JsonProperty("descripcion")
    @JsonPropertyDescription("Descripci\u00f3n")
    @Size(max = 1000)
    @NotNull
    public String descripcion;
    private final static long serialVersionUID = -8273820115160386130L;


    /**
     * Código retención MH
     * 
     */
    @Generated("jsonschema2pojo")
    public enum CodigoRetencionMH {

        _22("22"),
        C_4("C4"),
        C_9("C9");
        private final String value;
        private final static Map<String, CodigoRetencionMH> CONSTANTS = new HashMap<String, CodigoRetencionMH>();

        static {
            for (CodigoRetencionMH c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CodigoRetencionMH(String value) {
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
        public static CodigoRetencionMH fromValue(String value) {
            CodigoRetencionMH constant = CONSTANTS.get(value);
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
    public enum TipoDoc {

        _1(1),
        _2(2);
        private final Integer value;
        private final static Map<Integer, TipoDoc> CONSTANTS = new HashMap<Integer, TipoDoc>();

        static {
            for (TipoDoc c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoDoc(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static TipoDoc fromValue(Integer value) {
            TipoDoc constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }


    /**
     * Tipo de Documento Tributario Relacionado
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoDte {

        _14("14"),
        _03("03"),
        _01("01");
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
}
