
package sv.gov.cnr.factelectrcnrservice.models.dte.invalidacion;

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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Datos del emisor
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "nit",
    "nombre",
    "tipoEstablecimiento",
    "nomEstablecimiento",
    "codEstableMH",
    "codEstable",
    "codPuntoVentaMH",
    "codPuntoVenta",
    "telefono",
    "correo"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Emisor {

    /**
     * NIT, sin guiones
     * (Required)
     * 
     */
    @JsonProperty("nit")
    @JsonPropertyDescription("NIT, sin guiones")
    @Pattern(regexp = "^([0-9]{14}|[0-9]{9})$")
    @NotNull
    public String nit;
    /**
     * Nombre/Denominacion/Razon social
     * (Required)
     * 
     */
    @JsonProperty("nombre")
    @JsonPropertyDescription("Nombre/Denominacion/Razon social")
    @Size(min = 3, max = 250)
    @NotNull
    public String nombre;
    /**
     * Tipo establecimiento donde se emite documento
     * (Required)
     * 
     */
    @JsonProperty("tipoEstablecimiento")
    @JsonPropertyDescription("Tipo establecimiento donde se emite documento")
    @NotNull
    public Emisor.TipoEstablecimiento tipoEstablecimiento;
    /**
     * Nombre de establecimiento
     * (Required)
     * 
     */
    @JsonProperty("nomEstablecimiento")
    @JsonPropertyDescription("Nombre de establecimiento")
    @Size(min = 3, max = 150)
    @NotNull
    public String nomEstablecimiento;
    /**
     * Codigo, Numero o Identificador de establecimiento por MH
     * 
     */
    @JsonProperty("codEstableMH")
    @JsonPropertyDescription("Codigo, Numero o Identificador de establecimiento por MH")
    @Size(min = 4, max = 4)
    public String codEstableMH;
    /**
     * Codigo, Numero o Identificador de establecimiento por Contribuyente
     * (Required)
     * 
     */
    @JsonProperty("codEstable")
    @JsonPropertyDescription("Codigo, Numero o Identificador de establecimiento por Contribuyente")
    @Size(min = 1, max = 10)
    @NotNull
    public String codEstable;
    /**
     * Codigo, Numero o Identificador de punto de venta por MH
     * 
     */
    @JsonProperty("codPuntoVentaMH")
    @JsonPropertyDescription("Codigo, Numero o Identificador de punto de venta por MH")
    @Size(min = 4, max = 4)
    public String codPuntoVentaMH;
    /**
     * Codigo, Numero o Identificador de punto de venta por Contribuyente
     * (Required)
     * 
     */
    @JsonProperty("codPuntoVenta")
    @JsonPropertyDescription("Codigo, Numero o Identificador de punto de venta por Contribuyente")
    @Size(min = 1, max = 15)
    @NotNull
    public String codPuntoVenta;
    /**
     * Numero de telefono del emisor
     * (Required)
     * 
     */
    @JsonProperty("telefono")
    @JsonPropertyDescription("Numero de telefono del emisor")
    @Pattern(regexp = "^[0-9+;]{8,26}$")
    @Size(min = 8, max = 26)
    @NotNull
    public String telefono;
    /**
     * Correo electronico del emisor
     * (Required)
     * 
     */
    @JsonProperty("correo")
    @JsonPropertyDescription("Correo electronico del emisor")
    @Size(min = 3, max = 100)
    @NotNull
    public String correo;


    /**
     * Tipo establecimiento donde se emite documento
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoEstablecimiento {

        _01("01"),
        _02("02"),
        _04("04"),
        _07("07"),
        _20("20");
        private final String value;
        private final static Map<String, Emisor.TipoEstablecimiento> CONSTANTS = new HashMap<String, Emisor.TipoEstablecimiento>();

        static {
            for (Emisor.TipoEstablecimiento c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoEstablecimiento(String value) {
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
        public static Emisor.TipoEstablecimiento fromValue(String value) {
            Emisor.TipoEstablecimiento constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
