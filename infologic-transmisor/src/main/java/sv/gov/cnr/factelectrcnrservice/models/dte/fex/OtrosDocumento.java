
package sv.gov.cnr.factelectrcnrservice.models.dte.fex;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "codDocAsociado",
    "descDocumento",
    "detalleDocumento",
    "placaTrans",
    "modoTransp",
    "numConductor",
    "nombreConductor"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class OtrosDocumento implements Serializable
{

    /**
     * Documento asociado
     * (Required)
     * 
     */
    @JsonProperty("codDocAsociado")
    @JsonPropertyDescription("Documento asociado")
    @NotNull
    public OtrosDocumento.CodDocAsociado codDocAsociado;
    /**
     * Identificación del documento asociado
     * (Required)
     * 
     */
    @JsonProperty("descDocumento")
    @JsonPropertyDescription("Identificaci\u00f3n del documento asociado")
    @Size(max = 100)
    @NotNull
    public String descDocumento;
    /**
     * Descripción de documento asociado
     * (Required)
     * 
     */
    @JsonProperty("detalleDocumento")
    @JsonPropertyDescription("Descripci\u00f3n de documento asociado")
    @Size(max = 300)
    @NotNull
    public String detalleDocumento;
    /**
     * Número de identificación del transporte
     * (Required)
     * 
     */
    @JsonProperty("placaTrans")
    @JsonPropertyDescription("N\u00famero de identificaci\u00f3n del transporte")
    @Size(min = 5, max = 70)
    @NotNull
    public String placaTrans;
    /**
     * Modo de transporte
     * (Required)
     * 
     */
    @JsonProperty("modoTransp")
    @JsonPropertyDescription("Modo de transporte")
    public OtrosDocumento.ModoTransp modoTransp;
    /**
     * N documento de identificación del Conductor
     * (Required)
     * 
     */
    @JsonProperty("numConductor")
    @JsonPropertyDescription("N documento de identificaci\u00f3n del Conductor")
    @Size(min = 5, max = 100)
    @NotNull
    public String numConductor;
    /**
     * Nombre y apellidos del Conductor
     * (Required)
     * 
     */
    @JsonProperty("nombreConductor")
    @JsonPropertyDescription("Nombre y apellidos del Conductor")
    @Size(min = 5, max = 200)
    @NotNull
    public String nombreConductor;
    private final static long serialVersionUID = 6559979144735778167L;


    /**
     * Documento asociado
     * 
     */
    @Generated("jsonschema2pojo")
    public enum  CodDocAsociado {

        _1(1),
        _2(2),
        _3(3),
        _4(4);
        private final Integer value;
        private final static Map<Integer, CodDocAsociado> CONSTANTS = new HashMap<Integer, CodDocAsociado>();

        static {
            for (CodDocAsociado c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CodDocAsociado(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static CodDocAsociado fromValue(Integer value) {
            CodDocAsociado constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }


    /**
     * Modo de transporte
     * 
     */
    @Generated("jsonschema2pojo")
    public enum ModoTransp {

        _1(1),
        _2(2),
        _3(3),
        _4(4),
        _5(5),
        _6(6),
        _7(7);
        private final Integer value;
        private final static Map<Integer, ModoTransp> CONSTANTS = new HashMap<Integer, ModoTransp>();

        static {
            for (ModoTransp c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ModoTransp(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static ModoTransp fromValue(Integer value) {
            ModoTransp constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }

}
