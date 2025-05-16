
package sv.gov.cnr.factelectrcnrservice.models.dte.fc;

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
    "codigo",
    "descripcion",
    "valor"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Tributo implements Serializable
{

    /**
     * Resumen Código de Tributo
     * (Required)
     * 
     */
    @JsonProperty("codigo")
    @JsonPropertyDescription("Resumen C\u00f3digo de Tributo")
    @NotNull
    public Tributo.Codigo codigo;
    /**
     * Nombre del Tributo
     * (Required)
     * 
     */
    @JsonProperty("descripcion")
    @JsonPropertyDescription("Nombre del Tributo")
    @Size(min = 2, max = 150)
    @NotNull
    public String descripcion;
    /**
     * Valor del Tributo
     * (Required)
     * 
     */
    @JsonProperty("valor")
    @JsonPropertyDescription("Valor del Tributo")
    @NotNull
    public Double valor;
    private final static long serialVersionUID = -3679486156988884805L;


    /**
     * Resumen Código de Tributo
     * 
     */
    @Generated("jsonschema2pojo")
    public enum Codigo {

        C_3("C3"),
        _59("59"),
        _71("71"),
        D_1("D1"),
        C_8("C8"),
        C_5("C5"),
        C_6("C6"),
        C_7("C7"),
        D_5("D5"),
        _19("19"),
        _28("28"),
        _31("31"),
        _32("32"),
        _33("33"),
        _34("34"),
        _35("35"),
        _36("36"),
        _37("37"),
        _38("38"),
        _39("39"),
        _42("42"),
        _43("43"),
        _44("44"),
        _50("50"),
        _51("51"),
        _52("52"),
        _53("53"),
        _54("54"),
        _55("55"),
        _58("58"),
        _77("77"),
        _78("78"),
        _79("79"),
        _85("85"),
        _86("86"),
        _91("91"),
        _92("92"),
        A_1("A1"),
        A_5("A5"),
        A_7("A7"),
        A_9("A9");
        private final String value;
        private final static Map<String, Codigo> CONSTANTS = new HashMap<String, Codigo>();

        static {
            for (Codigo c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Codigo(String value) {
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
        public static Codigo fromValue(String value) {
            Codigo constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
