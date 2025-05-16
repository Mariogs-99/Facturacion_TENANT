
package sv.gov.cnr.factelectrcnrservice.models.dte.fse;

import java.io.Serializable;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.gov.cnr.factelectrcnrservice.models.dte.Direccion;


/**
 * Emisor
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "nit",
    "nrc",
    "nombre",
    "codActividad",
    "descActividad",
    "direccion",
    "telefono",
    "codEstableMH",
    "codEstable",
    "codPuntoVentaMH",
    "codPuntoVenta",
    "correo"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class Emisor implements Serializable
{

    /**
     * NIT (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("nit")
    @JsonPropertyDescription("NIT (Emisor)")
    @Pattern(regexp = "^([0-9]{14}|[0-9]{9})$")
    @NotNull
    public String nit;
    /**
     * NRC (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("nrc")
    @JsonPropertyDescription("NRC (Emisor)")
    @Pattern(regexp = "^[0-9]{1,8}$")
    @NotNull
    public String nrc;
    /**
     * Nombre, denominación o razón social del contribuyente (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("nombre")
    @JsonPropertyDescription("Nombre, denominaci\u00f3n o raz\u00f3n social del contribuyente (Emisor)")
    @Size(min = 1, max = 250)
    @NotNull
    public String nombre;
    /**
     * Código de Actividad Económica (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("codActividad")
    @JsonPropertyDescription("C\u00f3digo de Actividad Econ\u00f3mica (Emisor)")
    @Pattern(regexp = "^[0-9]{2,6}$")
    @NotNull
    public String codActividad;
    /**
     * Actividad Económica (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("descActividad")
    @JsonPropertyDescription("Actividad Econ\u00f3mica (Emisor)")
    @Size(min = 1, max = 150)
    @NotNull
    public String descActividad;
    /**
     * Dirección (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("direccion")
    @JsonPropertyDescription("Direcci\u00f3n (Emisor)")
    @Valid
    @NotNull
    public Direccion direccion;
    /**
     * Teléfono (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("telefono")
    @JsonPropertyDescription("Tel\u00e9fono (Emisor)")
    @Size(min = 8, max = 30)
    @NotNull
    public String telefono;
    /**
     * Código del establecimiento asignado por el MH
     * (Required)
     * 
     */
    @JsonProperty("codEstableMH")
    @JsonPropertyDescription("C\u00f3digo del establecimiento asignado por el MH")
    @Size(min = 4, max = 4)
    @NotNull
    public String codEstableMH;
    /**
     * Código del establecimiento asignado por el contribuyente
     * (Required)
     * 
     */
    @JsonProperty("codEstable")
    @JsonPropertyDescription("C\u00f3digo del establecimiento asignado por el contribuyente")
    @Size(min = 1, max = 10)
    @NotNull
    public String codEstable;
    /**
     * Código del Punto de Venta (Emisor) Asignado por el MH
     * (Required)
     * 
     */
    @JsonProperty("codPuntoVentaMH")
    @JsonPropertyDescription("C\u00f3digo del Punto de Venta (Emisor) Asignado por el MH")
    @Size(min = 4, max = 4)
    @NotNull
    public String codPuntoVentaMH;
    /**
     * Código del Punto de Venta (Emisor) asignado por el contribuyente
     * (Required)
     * 
     */
    @JsonProperty("codPuntoVenta")
    @JsonPropertyDescription("C\u00f3digo del Punto de Venta (Emisor) asignado por el contribuyente")
    @Size(min = 1, max = 15)
    @NotNull
    public String codPuntoVenta;
    /**
     * Correo electrónico (Emisor)
     * (Required)
     * 
     */
    @JsonProperty("correo")
    @JsonPropertyDescription("Correo electr\u00f3nico (Emisor)")
    @Size(min = 3, max = 100)
    @NotNull
    public String correo;
    private final static long serialVersionUID = -6441576825745050149L;

}
