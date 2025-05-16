package sv.gov.cnr.cnrpos.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import sv.gov.cnr.cnrpos.entities.ComprobantePago;
import sv.gov.cnr.cnrpos.entities.DocumentoAsociado;
import sv.gov.cnr.cnrpos.models.ClienteCNR;
import sv.gov.cnr.cnrpos.models.ItemDTO;
import sv.gov.cnr.cnrpos.models.enums.FormaDePago;
import sv.gov.cnr.cnrpos.models.enums.TipoDTE;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
public class TransaccionDTO {
    @NotNull(message = "idUser no puede ser nulo")
    @Positive
    private Long idUser;
    @NotNull(message = "idSucursal no puede ser nulo")
    @Positive
    private Long idSucursal;
    @Valid
    private ClienteCNR clienteCNR;
    @NotNull(message = "formaDePago no puede ser nulo")
    private FormaDePago formaDePago;
    @NotNull(message = "tipoDTE no puede ser nulo")
    private TipoDTE tipoDTE;
    private BigDecimal totalExento;
    private BigDecimal totalNosujeto;
    private BigDecimal totalGravado;
    private BigDecimal totalNogravado;
    private BigDecimal totalIVA;
    private BigDecimal descuentoGravado;
    private BigDecimal descuentoNosujeto;
    private BigDecimal descuentoExento;

    private BigDecimal ivaRetenido;
    private BigDecimal ivaPercivido;
    private BigDecimal rentaRetenido;
    private BigDecimal saldoAfavor;

    private Date       fechaTransaccion;
    private Timestamp horaTransaccion;


    @Size(max = 2000,message = "items debe contener al menos 1 elemento y máximo 2000")
   // @Valid
    private List<ItemDTO> items;

    @Size(max = 50,message = "Los documentos asociados no deben de ser mayor a 50")
    private List<DocumentoAsociadoDTO> documentosAsociados;

    private  List<ComprobantePagoDTO> comprobantePagos;

    private List<CuerpoDocCrDTO> cuerpoDocCrDTO;

    private String notas;

    private String estadoDte;
}
