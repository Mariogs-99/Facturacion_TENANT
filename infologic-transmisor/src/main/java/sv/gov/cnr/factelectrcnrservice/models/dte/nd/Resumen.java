
package sv.gov.cnr.factelectrcnrservice.models.dte.nd;

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
    "totalNoSuj",
    "totalExenta",
    "totalGravada",
    "subTotalVentas",
    "descuNoSuj",
    "descuExenta",
    "descuGravada",
    "totalDescu",
    "tributos",
    "subTotal",
    "ivaPerci1",
    "ivaRete1",
    "reteRenta",
    "montoTotalOperacion",
    "totalLetras",
    "condicionOperacion",
    "numPagoElectronico"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Resumen implements Serializable
{

    /**
     * Total de Operaciones no sujetas
     * (Required)
     * 
     */
    @JsonProperty("totalNoSuj")
    @JsonPropertyDescription("Total de Operaciones no sujetas")
    @NotNull
    public Double totalNoSuj;
    /**
     * Total de Operaciones exentas
     * (Required)
     * 
     */
    @JsonProperty("totalExenta")
    @JsonPropertyDescription("Total de Operaciones exentas")
    @NotNull
    public Double totalExenta;
    /**
     * Total de Operaciones Gravadas
     * (Required)
     * 
     */
    @JsonProperty("totalGravada")
    @JsonPropertyDescription("Total de Operaciones Gravadas")
    @NotNull
    public Double totalGravada;
    /**
     * Suma de operaciones sin impuestos
     * (Required)
     * 
     */
    @JsonProperty("subTotalVentas")
    @JsonPropertyDescription("Suma de operaciones sin impuestos")
    @NotNull
    public Double subTotalVentas;
    /**
     * Monto global de Descuento, Bonificación, Rebajas y otros a ventas no sujetas
     * (Required)
     * 
     */
    @JsonProperty("descuNoSuj")
    @JsonPropertyDescription("Monto global de Descuento, Bonificaci\u00f3n, Rebajas y otros a ventas no sujetas")
    @NotNull
    public Double descuNoSuj;
    /**
     * Monto global de Descuento, Bonificación, Rebajas y otros a ventas exentas
     * (Required)
     * 
     */
    @JsonProperty("descuExenta")
    @JsonPropertyDescription("Monto global de Descuento, Bonificaci\u00f3n, Rebajas y otros a ventas exentas")
    @NotNull
    public Double descuExenta;
    /**
     * Monto global de Descuento, Bonificación, Rebajas y otros a ventas gravadas
     * (Required)
     * 
     */
    @JsonProperty("descuGravada")
    @JsonPropertyDescription("Monto global de Descuento, Bonificaci\u00f3n, Rebajas y otros a ventas gravadas")
    @NotNull
    public Double descuGravada;
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
     * Resumen de tributos
     * (Required)
     * 
     */
    @JsonProperty("tributos")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("Resumen de tributos")
    @NotNull
    public Set<Tributo> tributos;
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
     * IVA Percibido
     * (Required)
     * 
     */
    @JsonProperty("ivaPerci1")
    @JsonPropertyDescription("IVA Percibido")
    @NotNull
    public Double ivaPerci1;
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
     * Monto Total de la Operación
     * (Required)
     * 
     */
    @JsonProperty("montoTotalOperacion")
    @JsonPropertyDescription("Monto Total de la Operaci\u00f3n")
    @NotNull
    public Double montoTotalOperacion;
    /**
     * Valor en Letras
     * (Required)
     * 
     */
    @JsonProperty("totalLetras")
    @JsonPropertyDescription("Valor en Letras")
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
     * Número de Pago Electrónico
     * (Required)
     * 
     */
    @JsonProperty("numPagoElectronico")
    @JsonPropertyDescription("N\u00famero de Pago Electr\u00f3nico")
    @Size(max = 100)
    @NotNull
    public String numPagoElectronico;
    private final static long serialVersionUID = -5633741283097380462L;


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
