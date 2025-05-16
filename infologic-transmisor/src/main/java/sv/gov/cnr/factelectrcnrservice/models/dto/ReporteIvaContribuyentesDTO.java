package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import sv.gov.cnr.factelectrcnrservice.models.enums.TipoReporte;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ReporteIvaContribuyentesDTO {

    private String FECHA_EMISION;
    private BigDecimal CLASE_DOCUMENTO;
    private String TIPO_DOCUMENTO;
    private String NUMERO_RESOLUCION;
    private String NUMERO_SERIE;
    private String NUMERO_DOCUMENTO;
    private String NUMERO_CONTROL;
    private String NIT;
    private String NOMBRE_CLIENTE;
    private Double TOTAL_EXENTO;
    private Double TOTAL_NO_SUJETO;
    private Double TOTAL_GRAVADO;
    private Double DEBITO_FISCAL;
    private Double VENTA_TERCEROS;
    private Double DEBITO_TERCEROS;
    private Double TOTAL_VENTAS;
    private String DUI;
    private BigDecimal ANEXO;


    public ReporteIvaContribuyentesDTO(String FECHA_EMISION, BigDecimal CLASE_DOCUMENTO, String TIPO_DOCUMENTO,
                                       String NUMERO_RESOLUCION, String NUMERO_SERIE, String NUMERO_DOCUMENTO,
                                       String NUMERO_CONTROL, String NIT, String NOMBRE_CLIENTE, Double TOTAL_EXENTO,
                                       Double TOTAL_NO_SUJETO, Double TOTAL_GRAVADO, Double DEBITO_FISCAL,
                                       Double VENTA_TERCEROS, Double DEBITO_TERCEROS, Double TOTAL_VENTAS, String DUI, BigDecimal ANEXO) {
        this.FECHA_EMISION = FECHA_EMISION;
        this.CLASE_DOCUMENTO = CLASE_DOCUMENTO;
        this.TIPO_DOCUMENTO = TIPO_DOCUMENTO;
        this.NUMERO_RESOLUCION = NUMERO_RESOLUCION;
        this.NUMERO_SERIE = NUMERO_SERIE;
        this.NUMERO_DOCUMENTO = NUMERO_DOCUMENTO;
        this.NUMERO_CONTROL = NUMERO_CONTROL;
        this.NIT = NIT;
        this.NOMBRE_CLIENTE = NOMBRE_CLIENTE;
        this.TOTAL_EXENTO = TOTAL_EXENTO;
        this.TOTAL_NO_SUJETO = TOTAL_NO_SUJETO;
        this.TOTAL_GRAVADO = TOTAL_GRAVADO;
        this.DEBITO_FISCAL = DEBITO_FISCAL;
        this.VENTA_TERCEROS = VENTA_TERCEROS;
        this.DEBITO_TERCEROS = DEBITO_TERCEROS;
        this.TOTAL_VENTAS = TOTAL_VENTAS;
        this.DUI = DUI;
        this.ANEXO = ANEXO;

    }
}
