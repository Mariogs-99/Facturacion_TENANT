
package sv.gov.cnr.factelectrcnrservice.models.dte.fex;

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
    "totalGravada",
    "descuento",
    "porcentajeDescuento",
    "totalDescu",
    "seguro",
    "flete",
    "montoTotalOperacion",
    "totalNoGravado",
    "totalPagar",
    "totalLetras",
    "condicionOperacion",
    "pagos",
    "codIncoterms",
    "descIncoterms",
    "numPagoElectronico",
    "observaciones"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Resumen implements Serializable
{

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
     * Monto global de Descuento, Bonificación, Rebajas y otros a ventas
     * (Required)
     * 
     */
    @JsonProperty("descuento")
    @JsonPropertyDescription("Monto global de Descuento, Bonificaci\u00f3n, Rebajas y otros a ventas")
    @NotNull
    public Double descuento;
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
     * Seguro
     * (Required)
     * 
     */
    @JsonProperty("seguro")
    @JsonPropertyDescription("Seguro")
    @NotNull
    public Double seguro;
    /**
     * Flete
     * (Required)
     * 
     */
    @JsonProperty("flete")
    @JsonPropertyDescription("Flete")
    @NotNull
    public Double flete;
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
     * Total Cargos / Abonos que no afectan la base imponible
     * (Required)
     * 
     */
    @JsonProperty("totalNoGravado")
    @JsonPropertyDescription("Total Cargos / Abonos que no afectan la base imponible")
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
     * INCOTERMS
     * (Required)
     * 
     */
    @JsonProperty("codIncoterms")
    @JsonPropertyDescription("INCOTERMS")
    @NotNull
    public String codIncoterms;
    /**
     * Descripción INCOTERMS
     * (Required)
     * 
     */
    @JsonProperty("descIncoterms")
    @JsonPropertyDescription("Descripci\u00f3n INCOTERMS")
    @Size(min = 3, max = 150)
    @NotNull
    public String descIncoterms;
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
    /**
     * Observaciones
     * (Required)
     * 
     */
    @JsonProperty("observaciones")
    @JsonPropertyDescription("Observaciones")
    @Size(max = 500)
    @NotNull
    public String observaciones;
    private final static long serialVersionUID = 1348024763820757105L;


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
