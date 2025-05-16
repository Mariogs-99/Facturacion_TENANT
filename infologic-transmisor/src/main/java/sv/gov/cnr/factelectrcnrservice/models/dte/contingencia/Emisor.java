package sv.gov.cnr.factelectrcnrservice.models.dte.contingencia;



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
 * Informacion del emisor.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "nit",
        "nombre",
        "nombreResponsable",
        "tipoDocResponsable",
        "numeroDocResponsable",
        "tipoEstablecimiento",
        "codEstableMH",
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
     * Nombre, denominación o  razón social del contribuyente (EMISOR)
     * (Required)
     *
     */
    @JsonProperty("nombre")
    @JsonPropertyDescription("Nombre, denominaci\u00f3n o  raz\u00f3n social del contribuyente (EMISOR)")
    @Size(min = 5, max = 250)
    @NotNull
    public String nombre;
    /**
     * NOMBRE DEL RESPONSABLE DEL ESTABLECIMIENTO.
     * (Required)
     *
     */
    @JsonProperty("nombreResponsable")
    @JsonPropertyDescription("NOMBRE DEL RESPONSABLE DEL ESTABLECIMIENTO.")
    @Size(min = 5, max = 100)
    @NotNull
    public String nombreResponsable;
    /**
     * Tipo documento de identificación del responsable CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO
     * (Required)
     *
     */
    @JsonProperty("tipoDocResponsable")
    @JsonPropertyDescription("Tipo documento de identificaci\u00f3n del responsable CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO")
    @NotNull
    public Emisor.TipoDocResponsable tipoDocResponsable;
    /**
     * NUMERO DE DOCUMENTO DE IDENTIFICACION DEL RESPONSABLE.
     * (Required)
     *
     */
    @JsonProperty("numeroDocResponsable")
    @JsonPropertyDescription("NUMERO DE DOCUMENTO DE IDENTIFICACION DEL RESPONSABLE.")
    @Size(min = 5, max = 25)
    @NotNull
    public String numeroDocResponsable;
    /**
     * TIPO DE ESTABLECIMIENTO (EMISOR) (QUE HA ENTRADO EN CONTINGENCIA)
     * (Required)
     *
     */
    @JsonProperty("tipoEstablecimiento")
    @JsonPropertyDescription("TIPO DE ESTABLECIMIENTO (EMISOR) (QUE HA ENTRADO EN CONTINGENCIA)")
    @NotNull
    public Emisor.TipoEstablecimiento tipoEstablecimiento;
    /**
     * Codigo, Numero o Identificador de establecimiento por MH
     *
     */
    @JsonProperty("codEstableMH")
    @JsonPropertyDescription("Codigo, Numero o Identificador de establecimiento por MH")
    @Size(min = 4, max = 4)
    public String codEstableMH;
    /**
     * Codigo, Numero o Identificador de punto de venta por Contribuyente
     *
     */
    @JsonProperty("codPuntoVenta")
    @JsonPropertyDescription("Codigo, Numero o Identificador de punto de venta por Contribuyente")
    @Size(min = 1, max = 15)
    public String codPuntoVenta;
    /**
     * Numero de telefono del emisor
     * (Required)
     *
     */
    @JsonProperty("telefono")
    @JsonPropertyDescription("Numero de telefono del emisor")
    @Size(min = 8, max = 30)
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
     * Tipo documento de identificación del responsable CAT-22: 36 - NIT, 13 - DUI, 02 - Carnet de residente, 03 - PASAPORTE, 37 - OTRO
     *
     */
    @Generated("jsonschema2pojo")
    public enum TipoDocResponsable {

        _36("36"),
        _13("13"),
        _02("02"),
        _03("03"),
        _37("37");
        private final String value;
        private final static Map<String, Emisor.TipoDocResponsable> CONSTANTS = new HashMap<String, Emisor.TipoDocResponsable>();

        static {
            for (Emisor.TipoDocResponsable c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TipoDocResponsable(String value) {
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
        public static Emisor.TipoDocResponsable fromValue(String value) {
            Emisor.TipoDocResponsable constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * TIPO DE ESTABLECIMIENTO (EMISOR) (QUE HA ENTRADO EN CONTINGENCIA)
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
