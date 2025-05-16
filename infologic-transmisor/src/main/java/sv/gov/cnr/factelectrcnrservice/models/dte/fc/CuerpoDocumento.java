
package sv.gov.cnr.factelectrcnrservice.models.dte.fc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "numItem",
    "tipoItem",
    "numeroDocumento",
    "cantidad",
    "codigo",
    "codTributo",
    "uniMedida",
    "descripcion",
    "precioUni",
    "montoDescu",
    "ventaNoSuj",
    "ventaExenta",
    "ventaGravada",
    "tributos",
    "psv",
    "noGravado",
    "ivaItem"
})
@Generated("jsonschema2pojo")
@Data
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
     * Número de documento relacionado
     * (Required)
     * 
     */
    @JsonProperty("numeroDocumento")
    @JsonPropertyDescription("N\u00famero de documento relacionado")
    @Size(min = 1, max = 36)
    public String numeroDocumento;
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
    public String codigo;
    /**
     * Tributo sujeto a cálculo de IVA
     * (Required)
     * 
     */
    @JsonProperty("codTributo")
    @JsonPropertyDescription("Tributo sujeto a c\u00e1lculo de IVA")
    @NotNull
    public CuerpoDocumento.CodTributo codTributo;
    /**
     * Unidad de Medida
     * (Required)
     * 
     */
    @JsonProperty("uniMedida")
    @JsonPropertyDescription("Unidad de Medida")
    @DecimalMin("1")
    @DecimalMax("99")
    @NotNull
    public Integer uniMedida;
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
     * Código del Tributo
     * (Required)
     * 
     */
    @JsonProperty("tributos")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("C\u00f3digo del Tributo")
    @Size(min = 1)
    @NotNull
    public Set<String> tributos;
    /**
     * Precio sugerido de venta
     * (Required)
     * 
     */
    @JsonProperty("psv")
    @JsonPropertyDescription("Precio sugerido de venta")
    public Double psv;
    /**
     * Cargos/Abonos que no afectan la base imponible
     * (Required)
     * 
     */
    @JsonProperty("noGravado")
    @JsonPropertyDescription("Cargos/Abonos que no afectan la base imponible")
    public Double noGravado;
    /**
     * IVA por ítem
     * (Required)
     * 
     */
    @JsonProperty("ivaItem")
    @JsonPropertyDescription("IVA por \u00edtem")
    @NotNull
    public Double ivaItem;
    private final static long serialVersionUID = -1612172230519135621L;


    /**
     * Tributo sujeto a cálculo de IVA
     * 
     */
    @Generated("jsonschema2pojo")
    public enum CodTributo {

        A_8("A8"),
        _57("57"),
        _90("90"),
        D_4("D4"),
        D_5("D5"),
        _25("25"),
        A_6("A6");
        private final String value;
        private final static Map<String, CodTributo> CONSTANTS = new HashMap<String, CodTributo>();

        static {
            for (CodTributo c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CodTributo(String value) {
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
        public static CodTributo fromValue(String value) {
            CodTributo constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Tipo de ítem
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoItem {

        _1(1),
        _2(2),
        _3(3),
        _4(4);
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

}
