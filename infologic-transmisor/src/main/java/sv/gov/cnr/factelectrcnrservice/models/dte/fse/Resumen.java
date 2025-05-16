
package sv.gov.cnr.factelectrcnrservice.models.dte.fse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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


/**
 * Resumen
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "totalCompra",
    "descu",
    "totalDescu",
    "subTotal",
    "ivaRete1",
    "reteRenta",
    "totalPagar",
    "totalLetras",
    "condicionOperacion",
    "pagos",
    "observaciones"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Resumen implements Serializable
{

    /**
     * Total de Operaciones
     * (Required)
     * 
     */
    @JsonProperty("totalCompra")
    @JsonPropertyDescription("Total de Operaciones")
    @NotNull
    public Double totalCompra;
    /**
     * Monto global de Descuento, Bonificación, Rebajas y otros al total de operaciones.
     * (Required)
     * 
     */
    @JsonProperty("descu")
    @JsonPropertyDescription("Monto global de Descuento, Bonificaci\u00f3n, Rebajas y otros al total de operaciones.")
    @NotNull
    public Double descu;
    /**
     * Total del monto de Descuento, Bonificación, Rebajas
     * (Required)
     * 
     */
    @JsonProperty("totalDescu")
    @JsonPropertyDescription("Total del monto de Descuento, Bonificaci\u00f3n, Rebajas")
    @NotNull
    public Double totalDescu;
    /**
     * Sub-Total
     * (Required)
     * 
     */
    @JsonProperty("subTotal")
    @JsonPropertyDescription("Sub-Total")
    @NotNull
    public Double subTotal;
    /**
     * IVA Retenido
     * (Required)
     * 
     */
    @JsonProperty("ivaRete1")
    @JsonPropertyDescription("IVA Retenido")
    @NotNull
    public Double ivaRete1;
    /**
     * Retención Renta
     * (Required)
     * 
     */
    @JsonProperty("reteRenta")
    @JsonPropertyDescription("Retenci\u00f3n Renta")
    @NotNull
    public Double reteRenta;
    /**
     * Total a Pagar
     * (Required)
     * 
     */
    @JsonProperty("totalPagar")
    @JsonPropertyDescription("Total a Pagar")
    @NotNull
    public Double totalPagar;
    /**
     * Total en Letras
     * (Required)
     * 
     */
    @JsonProperty("totalLetras")
    @JsonPropertyDescription("Total en Letras")
    @Size(max = 200)
    @NotNull
    public String totalLetras;
    /**
     * Condición de la Operación
     * (Required)
     * 
     */
    @JsonProperty("condicionOperacion")
    @JsonPropertyDescription("Condici\u00f3n de la Operaci\u00f3n")
    @NotNull
    public Resumen.CondicionOperacion condicionOperacion;
    /**
     * Pagos
     * (Required)
     * 
     */
    @JsonProperty("pagos")
    @JsonPropertyDescription("Pagos")
    @Size(min = 1)
    @NotNull
    public List<Pago> pagos;
    /**
     * Observaciones
     * (Required)
     * 
     */
    @JsonProperty("observaciones")
    @JsonPropertyDescription("Observaciones")
    @Size(max = 3000)
    @NotNull
    public String observaciones;
    private final static long serialVersionUID = 834945776564555229L;


    /**
     * Condición de la Operación
     * 
     */
    @Generated("jsonschema2pojo")
    public enum CondicionOperacion {

        _1(1),
        _2(2),
        _3(3);
        private final Integer value;
        private final static Map<Integer, CondicionOperacion> CONSTANTS = new HashMap<Integer, CondicionOperacion>();

        static {
            for (CondicionOperacion c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CondicionOperacion(Integer value) {
            this.value = value;
        }

        @JsonValue
        public Integer value() {
            return this.value;
        }

        @JsonCreator
        public static CondicionOperacion fromValue(Integer value) {
            CondicionOperacion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value +""));
            } else {
                return constant;
            }
        }

    }

}
