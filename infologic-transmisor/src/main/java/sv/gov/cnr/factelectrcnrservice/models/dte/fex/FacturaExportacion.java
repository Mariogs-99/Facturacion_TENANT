
package sv.gov.cnr.factelectrcnrservice.models.dte.fex;

import java.io.Serializable;
import java.util.List;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.gov.cnr.factelectrcnrservice.models.dte.Apendice;
import sv.gov.cnr.factelectrcnrservice.models.dte.VentaTercero;


/**
 * Factura de Exportación Electrónica v1
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "identificacion",
    "emisor",
    "receptor",
    "otrosDocumentos"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
public class FacturaExportacion implements Serializable
{

    /**
     * Identificación
     * (Required)
     * 
     */
    @JsonProperty("identificacion")
    @JsonPropertyDescription("Identificaci\u00f3n")
    @Valid
    @NotNull
    public Identificacion identificacion;
    /**
     * Emisor
     * (Required)
     * 
     */
    @JsonProperty("emisor")
    @JsonPropertyDescription("Emisor")
    @Valid
    @NotNull
    public Emisor emisor;
    /**
     * Receptor
     * (Required)
     * 
     */
    @JsonProperty("receptor")
    @JsonPropertyDescription("Receptor")
    @NotNull
    public Receptor receptor;
    /**
     * Documentos Asociados
     * (Required)
     * 
     */
    @JsonProperty("otrosDocumentos")
    @JsonPropertyDescription("Documentos Asociados")
    @Size(min = 1, max = 20)
    @NotNull
    public List<OtrosDocumento> otrosDocumentos;
    @JsonProperty("ventaTercero")
    @JsonPropertyDescription("Ventas por cuenta de terceros")
    @NotNull
    public VentaTercero ventaTercero;
    /**
     * Cuerpo del Documento
     * (Required)
     *
     */
    @JsonProperty("cuerpoDocumento")
    @JsonPropertyDescription("Cuerpo del Documento")
    @Size(min = 1, max = 2000)
    @Valid
    @NotNull
    public List<CuerpoDocumento> cuerpoDocumento;
    /**
     * Resumen
     * (Required)
     *
     */
    @JsonProperty("resumen")
    @JsonPropertyDescription("Resumen")
    @Valid
    @NotNull
    public Resumen resumen;
    /**
     * Apéndice
     * (Required)
     *
     */
    @JsonProperty("apendice")
    @JsonPropertyDescription("Ap\u00e9ndice")
    @Size(min = 1, max = 10)
    @NotNull
    public List<Apendice> apendice;
    private final static long serialVersionUID = -8249375340872073211L;

}
