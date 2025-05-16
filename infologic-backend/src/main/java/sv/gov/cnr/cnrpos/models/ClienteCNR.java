package sv.gov.cnr.cnrpos.models;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import sv.gov.cnr.cnrpos.models.enums.TipoDocumentoReceptor;
import sv.gov.cnr.cnrpos.models.enums.TipoPersona;

@Data
public class ClienteCNR {//Modelo para el cliente que viene de la API del CNR
    private TipoPersona tipoPersona;
    @NotEmpty
    private String nombre;
    //campos persona natural
    private TipoDocumentoReceptor tipoDocumento;////Ligado al CAT-022 Tipo de documento de identificación del Receptor. (se necesita requerir al cnr que envien los mismos valores que estan en el enum, que a su vez son los mismos del catalogo de hacienda)
    private String numeroDocumento;
    private String telefono;
    @NotEmpty
    @Email
    private String email;
    //campos persona juridica
    private String nombreComercial;
    private String nit;
    //Si esta inscrito en el registro de iva necesita lo siguiente:
    private String pais;//El Salvador
    private String departamento;
    private String municipio;
    private String direccion;
    private String numeroCasa;
    private String apartamentoLocal;
    private String calle;
    private String colonia;
    private String actividadEconomica;
    private String nrc;
    private Boolean esExtranjero;
    private Boolean esGranContribuyente;
    private Boolean esGobierno;
    private Boolean esConsumidorFinal;
    //Si el cliente tiene descuento
    @DecimalMin(value = "0.0", inclusive = true, message = "El porcentajeDescuento debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "El porcentajeDescuento debe ser menor o igual a 100")
    private Double porcentajeDescuento = 0.0; // Inicializado con 0
    private String descripcionDescuento;
    Long idCliente = 0L;
}
