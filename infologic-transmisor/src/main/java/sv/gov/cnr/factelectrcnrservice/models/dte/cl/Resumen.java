
package sv.gov.cnr.factelectrcnrservice.models.dte.cl;

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
import jakarta.validation.constraints.AssertTrue;
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
    "totalExportacion",
    "subTotalVentas",
    "tributos",
    "montoTotalOperacion",
    "ivaPerci",
    "total",
    "totalLetras",
    "condicionOperacion"
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
     * Total de Operaciones Exportación
     * (Required)
     * 
     */
    @JsonProperty("totalExportacion")
    @JsonPropertyDescription("Total de Operaciones Exportaci\u00f3n")
    @NotNull
    public Double totalExportacion;
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
     * Monto Total de la Operación
     * (Required)
     * 
     */
    @JsonProperty("montoTotalOperacion")
    @JsonPropertyDescription("Monto Total de la Operaci\u00f3n")
    @NotNull
    public Double montoTotalOperacion;
    /**
     * IVA Percibido liquidado
     * (Required)
     * 
     */
    @JsonProperty("ivaPerci")
    @JsonPropertyDescription("IVA Percibido liquidado")
    @NotNull
    public Double ivaPerci;
    /**
     * Total a Pagar
     * (Required)
     * 
     */
    @JsonProperty("total")
    @JsonPropertyDescription("Total a Pagar")
    @NotNull
    public Double total;
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
    private final static long serialVersionUID = -5397801780753104596L;


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
    @AssertTrue(message = "Si total es 0, entonces condicionOperacion debe ser 1.")
    private boolean isCondicionOperacionValid() {
        // Lógica de validación
        return total != 0;
    }

}
