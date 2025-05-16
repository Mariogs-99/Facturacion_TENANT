package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DocumentosDTO {

    private Long idTransaccion;
    private String tipoDte;
    private String codigoGeneracion;
    private String numeroDte;
    private String fechaEmision;
    private BigDecimal totalTransaccion;
    private Long tipoGeneracion;


    public DocumentosDTO(Long idTransaccion, String tipoDte, String codigoGeneracion, String numeroDte, String fechaEmision, BigDecimal totalTransaccion, Long tipoGeneracion) {
        this.idTransaccion = idTransaccion;
        this.tipoDte = tipoDte;
        this.codigoGeneracion = codigoGeneracion;
        this.numeroDte = numeroDte;
        this.fechaEmision = fechaEmision;
        this.totalTransaccion = totalTransaccion;
        this.tipoGeneracion = tipoGeneracion;
    }
}
