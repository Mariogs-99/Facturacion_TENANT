
package sv.gov.cnr.factelectrcnrservice.models.dte.ccf;

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
import sv.gov.cnr.factelectrcnrservice.models.dte.Apendice;
import sv.gov.cnr.factelectrcnrservice.models.dte.VentaTercero;


/**
 * Comprobante de Crédito Fiscal Electrónica v3
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "identificacion",
    "documentoRelacionado",
    "emisor",
    "receptor",
    "otrosDocumentos",
    "ventaTercero",
    "cuerpoDocumento",
    "resumen",
    "extension",
    "apendice"
})
@Generated("jsonschema2pojo")
@Data
public class ComprobanteCreditoFiscal implements Serializable
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
     * Documentos Relacionados
     * (Required)
     * 
     */
    @JsonProperty("documentoRelacionado")
    @JsonPropertyDescription("Documentos Relacionados")
    @Size(min = 1, max = 50)
    @NotNull
    public List<DocumentoRelacionado> documentoRelacionado;
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
    @Valid
    @NotNull
    public Receptor receptor;
    /**
     * Documentos Asociados
     * (Required)
     * 
     */
    @JsonProperty("otrosDocumentos")
    @JsonPropertyDescription("Documentos Asociados")
    @Size(min = 1, max = 10)
    @Valid
    @NotNull
    public List<OtrosDocumento> otrosDocumentos;
    /**
     * Ventas por cuenta de terceros
     * (Required)
     *
     */
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
     * Extensión
     * (Required)
     *
     */
    @JsonProperty("extension")
    @JsonPropertyDescription("Extensi\u00f3n")
    @NotNull
    public Extension extension;
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
    private final static long serialVersionUID = -6266692934989476404L;

}
