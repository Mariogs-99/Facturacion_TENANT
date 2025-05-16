
package sv.gov.cnr.factelectrcnrservice.models.dte.fse;

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
    "tipoItem",
    "cantidad",
    "codigo",
    "uniMedida",
    "descripcion",
    "precioUni",
    "montoDescu",
    "compra"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class CuerpoDocumento implements Serializable
{

    /**
     * N° ítem
     * (Required)
     * 
     */
    @JsonProperty("numItem")
    @JsonPropertyDescription("N\u00b0 \u00edtem")
    @DecimalMin("1")
    @DecimalMax("2000")
    @NotNull
    public Integer numItem;
    /**
     * Tipo de ítem
     * (Required)
     * 
     */
    @JsonProperty("tipoItem")
    @JsonPropertyDescription("Tipo de \u00edtem")
    @NotNull
    public CuerpoDocumento.TipoItem tipoItem;
    /**
     * Cantidad
     * (Required)
     * 
     */
    @JsonProperty("cantidad")
    @JsonPropertyDescription("Cantidad")
    @NotNull
    public Double cantidad;
    /**
     * Código
     * (Required)
     * 
     */
    @JsonProperty("codigo")
    @JsonPropertyDescription("C\u00f3digo")
    @Size(min = 1, max = 25)
    @NotNull
    public String codigo;
    /**
     * Unidad de Medida
     * (Required)
     * 
     */
    @JsonProperty("uniMedida")
    @JsonPropertyDescription("Unidad de Medida")
    @NotNull
    public CuerpoDocumento.UniMedida uniMedida;
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
    /**
     * Precio Unitario
     * (Required)
     * 
     */
    @JsonProperty("precioUni")
    @JsonPropertyDescription("Precio Unitario")
    @NotNull
    public Double precioUni;
    /**
     * Descuento, Bonificación, Rebajas por ítem
     * (Required)
     * 
     */
    @JsonProperty("montoDescu")
    @JsonPropertyDescription("Descuento, Bonificaci\u00f3n, Rebajas por \u00edtem")
    @NotNull
    public Double montoDescu;
    /**
     * Ventas
     * (Required)
     * 
     */
    @JsonProperty("compra")
    @JsonPropertyDescription("Ventas")
    @NotNull
    public Double compra;
    private final static long serialVersionUID = -3716041908406522608L;


    /**
     * Tipo de ítem
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoItem {

        _1(1),
        _2(2),
        _3(3);
        private final Integer value;
        private final static Map<Integer, TipoItem> CONSTANTS = new HashMap<Integer, TipoItem>();

        static {
            for (TipoItem c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoItem(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static TipoItem fromValue(Integer value) {
            TipoItem constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }


    /**
     * Unidad de Medida
     * 
     */
    @Generated("jsonschema2pojo")
    public enum UniMedida {

        _1(1),
        _2(2),
        _3(3),
        _4(4),
        _5(5),
        _6(6),
        _8(8),
        _9(9),
        _10(10),
        _11(11),
        _12(12),
        _13(13),
        _14(14),
        _15(15),
        _16(16),
        _17(17),
        _18(18),
        _19(19),
        _20(20),
        _21(21),
        _22(22),
        _23(23),
        _24(24),
        _25(25),
        _26(26),
        _27(27),
        _29(29),
        _30(30),
        _31(31),
        _32(32),
        _33(33),
        _34(34),
        _35(35),
        _36(36),
        _37(37),
        _38(38),
        _39(39),
        _40(40),
        _42(42),
        _43(43),
        _44(44),
        _45(45),
        _46(46),
        _47(47),
        _49(49),
        _50(50),
        _51(51),
        _52(52),
        _53(53),
        _54(54),
        _55(55),
        _56(56),
        _57(57),
        _58(58),
        _59(59),
        _99(99);
        private final Integer value;
        private final static Map<Integer, UniMedida> CONSTANTS = new HashMap<Integer, UniMedida>();

        static {
            for (UniMedida c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        UniMedida(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static UniMedida fromValue(Integer value) {
            UniMedida constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }

}
