
package sv.gov.cnr.factelectrcnrservice.models.dte.ccf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


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
    "porcentajeDescuento",
    "totalDescu",
    "tributos",
    "subTotal",
    "ivaPerci1",
    "ivaRete1",
    "reteRenta",
    "montoTotalOperacion",
    "totalNoGravado",
    "totalPagar",
    "totalLetras",
    "saldoFavor",
    "condicionOperacion",
    "pagos",
    "numPagoElectronico"
})
@Generated("jsonschema2pojo")
@Data
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
     * Total de Operaciones Exentas
     * (Required)
     * 
     */
    @JsonProperty("totalExenta")
    @JsonPropertyDescription("Total de Operaciones Exentas")
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
     * Monto de Descuento, Bonificación, Rebajas y otros a ventas exentas
     * (Required)
     * 
     */
    @JsonProperty("descuExenta")
    @JsonPropertyDescription("Monto de Descuento, Bonificaci\u00f3n, Rebajas y otros a ventas exentas")
    @NotNull
    public Double descuExenta;
    /**
     * Monto de Descuento, Bonificación, Rebajas y otros a ventas gravadas
     * (Required)
     * 
     */
    @JsonProperty("descuGravada")
    @JsonPropertyDescription("Monto de Descuento, Bonificaci\u00f3n, Rebajas y otros a ventas gravadas")
    @NotNull
    public Double descuGravada;
    /**
     * Porcentaje del monto global de Descuento, Bonificación, Rebajas y otros
     * (Required)
     * 
     */
    @JsonProperty("porcentajeDescuento")
    @JsonPropertyDescription("Porcentaje del monto global de Descuento, Bonificaci\u00f3n, Rebajas y otros")
    @NotNull
    public Double porcentajeDescuento;
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
     * Resumen de Tributos
     * (Required)
     * 
     */
    @JsonProperty("tributos")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    @JsonPropertyDescription("Resumen de Tributos")
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
     * Total Cargos/Abonos que no afectan la base imponible
     * (Required)
     * 
     */
    @JsonProperty("totalNoGravado")
    @JsonPropertyDescription("Total Cargos/Abonos que no afectan la base imponible")
    @NotNull
    public Double totalNoGravado;
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
     * Saldo a Favor
     * (Required)
     * 
     */
    @JsonProperty("saldoFavor")
    @JsonPropertyDescription("Saldo a Favor")
    @NotNull
    public Double saldoFavor;
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
     * Número de Pago Electrónico
     * (Required)
     * 
     */
    @JsonProperty("numPagoElectronico")
    @JsonPropertyDescription("N\u00famero de Pago Electr\u00f3nico")
    @Size(max = 100)
    @NotNull
    public String numPagoElectronico;
    private final static long serialVersionUID = 2847884894901721055L;


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
    @AssertTrue(message = "Si condicionOperacion es 2, entonces pagos debe contener al menos un elemento con plazo de tipo string y periodo de tipo número.")
    private boolean isValidCondicionOperacion2() {
        if (condicionOperacion != null && condicionOperacion == CondicionOperacion._2) {
            return pagos != null && !pagos.isEmpty() && isValidPagosList();
        }
        return true;
    }

    private boolean isValidPagosList() {
        for (Pago pago : pagos) {
            if (!isValidPago(pago)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPago(Pago pago) {
        return pago != null && pago.plazo != null && pago.periodo != null;
    }

    @AssertTrue(message = "Si totalGravada es mayor que 0, entonces ivaPerci1 y ivaRete1 deben ser menores o iguales a 0.")
    private boolean isValidIvaValues() {
        if (totalGravada != null && totalGravada > 0) {
            return ivaPerci1 != null && ivaPerci1 <= 0 && ivaRete1 != null && ivaRete1 <= 0;
        }
        return true;
    }

    @AssertTrue(message = "Si totalPagar está en el rango de 0 a 0, entonces condicionOperacion debe ser 1.")
    private boolean isValidCondicionOperacionForTotalPagar() {
        if (totalPagar != null && totalPagar >= 0 && totalPagar <= 0) {
            return condicionOperacion == CondicionOperacion._1;
        }
        return true;
    }

}
