
package sv.gov.cnr.factelectrcnrservice.models.dte.ccf;

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

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "numItem",
    "tipoItem",
    "numeroDocumento",
    "codigo",
    "codTributo",
    "descripcion",
    "cantidad",
    "uniMedida",
    "precioUni",
    "montoDescu",
    "ventaNoSuj",
    "ventaExenta",
    "ventaGravada",
    "tributos",
    "psv",
    "noGravado"
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
    @DecimalMin(value = "1", message = "El número de ítem debe ser mayor o igual a 1")
    @DecimalMax(value = "2000", message = "El número de ítem debe ser menor o igual a 2000")
    @NotNull(message = "El número de ítem es obligatorio")
    public Integer numItem;
    /**
     * Tipo de ítem
     * (Required)
     * 
     */
    @JsonProperty("tipoItem")
    @JsonPropertyDescription("Tipo de \u00edtem")
    @NotNull(message = "El tipo de ítem es obligatorio")
    public CuerpoDocumento.TipoItem tipoItem;
    /**
     * Número de documento relacionado
     * (Required)
     * 
     */
    @JsonProperty("numeroDocumento")
    @JsonPropertyDescription("N\u00famero de documento relacionado")
    @Size(min = 1, max = 36, message = "El número de documento debe tener entre 1 y 36 caracteres")
    @NotNull(message = "El número de documento es obligatorio")
    public String numeroDocumento;
    /**
     * Código
     * (Required)
     * 
     */
    @JsonProperty("codigo")
    @JsonPropertyDescription("C\u00f3digo")
    @Size(min = 1, max = 25, message = "El código debe tener entre 1 y 25 caracteres")
    @NotNull(message = "El código es obligatorio")
    public String codigo;
    /**
     * Tributo sujeto a cálculo de IVA.
     * (Required)
     * 
     */
    @JsonProperty("codTributo")
    @JsonPropertyDescription("Tributo sujeto a c\u00e1lculo de IVA.")
    @NotNull(message = "El código de tributo es obligatorio")
    public CuerpoDocumento.CodTributo codTributo;
    /**
     * Descripción
     * (Required)
     * 
     */
    @JsonProperty("descripcion")
    @JsonPropertyDescription("Descripci\u00f3n")
    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    @NotNull(message = "La descripción es obligatoria")
    public String descripcion;
    /**
     * Cantidad
     * (Required)
     * 
     */
    @JsonProperty("cantidad")
    @JsonPropertyDescription("Cantidad")
    @NotNull(message = "La cantidad es obligatoria")
    public Double cantidad;
    /**
     * Unidad de Medida
     * (Required)
     * 
     */
    @JsonProperty("uniMedida")
    @JsonPropertyDescription("Unidad de Medida")
    @DecimalMin(value = "1", message = "La unidad de medida debe ser mayor o igual a 1")
    @DecimalMax(value = "99", message = "La unidad de medida debe ser menor o igual a 99")
    @NotNull(message = "La unidad de medida es obligatoria")
    public Integer uniMedida;
    /**
     * Precio Unitario
     * (Required)
     * 
     */
    @JsonProperty("precioUni")
    @JsonPropertyDescription("Precio Unitario")
    @NotNull(message = "El precio unitario es obligatorio")
    public Double precioUni;
    /**
     * Descuento, Bonificación, Rebajas por ítem
     * (Required)
     * 
     */
    @JsonProperty("montoDescu")
    @JsonPropertyDescription("Descuento, Bonificaci\u00f3n, Rebajas por \u00edtem")
    @NotNull(message = "El monto de descuento es obligatorio")
    public Double montoDescu;
    /**
     * Ventas No Sujetas
     * (Required)
     * 
     */
    @JsonProperty("ventaNoSuj")
    @JsonPropertyDescription("Ventas No Sujetas")
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
    @Size(min = 1, message = "Debe haber al menos un código de tributo")
    @NotNull(message = "Los códigos de tributo son obligatorios")
    // TODO: verificar si es necesario validar el catálogo de tributos
    public Set<String> tributos;
    /**
     * Precio sugerido de venta
     * (Required)
     * 
     */
    @JsonProperty("psv")
    @JsonPropertyDescription("Precio sugerido de venta")
    @NotNull
    public Double psv;
    /**
     * Cargos / Abonos que no afectan la base imponible
     * (Required)
     * 
     */
    @JsonProperty("noGravado")
    @JsonPropertyDescription("Cargos / Abonos que no afectan la base imponible")
    @NotNull
    public Double noGravado;
    private final static long serialVersionUID = -2313676559459120721L;


    /**
     * Tributo sujeto a cálculo de IVA.
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
    @AssertTrue(message = "Si ventaGravada es mayor que 0, tributos debe ser una lista no nula y con al menos un elemento.")
    private boolean isValidTributos() {
        if (ventaGravada != null && ventaGravada > 0) {
            return tributos != null && !tributos.isEmpty();
        }
        return true;
    }

    @AssertTrue(message = "Si tipoItem es 4, uniMedida debe ser 99, codTributo debe ser una cadena, y tributos debe contener solo el valor '20'.")
    private boolean isValidTipoItem4() {
        if (tipoItem != null && tipoItem == TipoItem._4) {
            return uniMedida != null && uniMedida == 99 &&
                    codTributo != null && tributos != null && tributos.size() == 1 && tributos.contains("20");
        }
        return true;
    }

    @AssertTrue(message = "Si tipoItem no es 4, codTributo debe ser nulo y tributos debe contener solo valores permitidos.")
    private boolean isValidTipoItemNot4() {
        if (tipoItem == null || tipoItem != TipoItem._4) {
            return codTributo == null && isValidTributosEnumValues();
        }
        return true;
    }

    private boolean isValidTributosEnumValues() {
        if (tributos != null) {
            for (String tributo : tributos) {
                if (!isValidTributoEnumValue(tributo)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidTributoEnumValue(String tributo) {
        return tributo != null && (
                tributo.equals("20") || tributo.equals("C3") || tributo.equals("59") ||
                        // ... (otros valores permitidos)
                        tributo.equals("A9")
        );
    }

}
