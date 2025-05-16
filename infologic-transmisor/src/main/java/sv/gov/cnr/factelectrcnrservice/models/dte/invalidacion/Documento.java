
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
 * Datos del documento a Invalidar
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "tipoDte",
    "codigoGeneracion",
    "selloRecibido",
    "numeroControl",
    "fecEmi",
    "montoIva",
    "codigoGeneracionR",
    "tipoDocumento",
    "numDocumento",
    "nombre",
    "telefono",
    "correo"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Documento {

    /**
     * Tipo de documento
     * (Required)
     * 
     */
    @JsonProperty("tipoDte")
    @JsonPropertyDescription("Tipo de documento")
    @NotNull
    //public Documento.TipoDte tipoDte;
    public String tipoDte;
    /**
     * Codigo Generacion
     * (Required)
     * 
     */
    @JsonProperty("codigoGeneracion")
    @JsonPropertyDescription("Codigo Generacion")
    @Pattern(regexp = "^[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}$")
    @Size(min = 36, max = 36)
    @NotNull
    public String codigoGeneracion;
    /**
     * Sello de recepcion
     * (Required)
     * 
     */
    @JsonProperty("selloRecibido")
    @JsonPropertyDescription("Sello de recepcion")
    @Pattern(regexp = "^[A-Z0-9]{40}$")
    @Size(min = 40, max = 40)
    @NotNull
    public String selloRecibido;
    /**
     * Numero control
     * (Required)
     * 
     */
    @JsonProperty("numeroControl")
    @JsonPropertyDescription("Numero control")
    @Pattern(regexp = "^DTE-0[0-9]|1[0-2]-[A-Z0-9]{8}-[0-9]{15}$")
    @Size(min = 31, max = 31)
    @NotNull
    public String numeroControl;
    /**
     * Fecha de emision (formato yyyy-mm-dd)
     * (Required)
     * 
     */
    @JsonProperty("fecEmi")
    @JsonPropertyDescription("Fecha de emision (formato yyyy-mm-dd)")
    @NotNull
    public String fecEmi;
    /**
     * Monto IVA
     * (Required)
     * 
     */
    @JsonProperty("montoIva")
    @JsonPropertyDescription("Monto IVA")
    @NotNull
    public Double montoIva;
    /**
     * Codigo Generacion que reemplaza
     * (Required)
     * 
     */
    @JsonProperty("codigoGeneracionR")
    @JsonPropertyDescription("Codigo Generacion que reemplaza")
    @Pattern(regexp = "^[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}$")
    @Size(min = 36, max = 36)
    @NotNull
    public String codigoGeneracionR;
    /**
     * Tipo documento de identificación CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO
     * (Required)
     * 
     */
    @JsonProperty("tipoDocumento")
    @JsonPropertyDescription("Tipo documento de identificaci\u00f3n CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO")
    @NotNull
    public Documento.TipoDocumento tipoDocumento;
    /**
     * Número de documento de Identificación 
     * (Required)
     * 
     */
    @JsonProperty("numDocumento")
    @JsonPropertyDescription("N\u00famero de documento de Identificaci\u00f3n ")
    @Size(min = 3, max = 20)
    @NotNull
    public String numDocumento;
    /**
     * Nombre/Denominacion/Razon social
     * (Required)
     * 
     */
    @JsonProperty("nombre")
    @JsonPropertyDescription("Nombre/Denominacion/Razon social")
    @Size(min = 5, max = 200)
    @NotNull
    public String nombre;
    /**
     * Numero de telefono del receptor
     * 
     */
    @JsonProperty("telefono")
    @JsonPropertyDescription("Numero de telefono del receptor")
    @Pattern(regexp = "^[0-9+;]{8,50}$")
    @Size(min = 8, max = 50)
    public String telefono;
    /**
     * Correo electronico del receptor
     * 
     */
    @JsonProperty("correo")
    @JsonPropertyDescription("Correo electronico del receptor")
    @Size(max = 100)
    public String correo;


    /**
     * Tipo documento de identificación CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoDocumento {

        _36("36"),
        _13("13"),
        _02("02"),
        _03("03"),
        _37("37");
        private final String value;
        private final static Map<String, Documento.TipoDocumento> CONSTANTS = new HashMap<String, Documento.TipoDocumento>();

        static {
            for (Documento.TipoDocumento c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoDocumento(String value) {
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
        public static Documento.TipoDocumento fromValue(String value) {
            Documento.TipoDocumento constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Tipo de documento
     * 
     */
    @Generated("jsonschema2pojo")
    public enum TipoDte {

        _01("01"),
        _03("03"),
        _04("04"),
        _05("05"),
        _06("06"),
        _07("07"),
        _08("08"),
        _09("09"),
        _10("10"),
        _11("11"),
        _14("14"),
        _15("15");
        private final String value;
        private final static Map<String, Documento.TipoDte> CONSTANTS = new HashMap<String, Documento.TipoDte>();

        static {
            for (Documento.TipoDte c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoDte(String value) {
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
        public static Documento.TipoDte fromValue(String value) {
            Documento.TipoDte constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
