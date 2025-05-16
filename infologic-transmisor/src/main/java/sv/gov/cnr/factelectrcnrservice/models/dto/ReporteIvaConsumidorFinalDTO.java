package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ReporteIvaConsumidorFinalDTO {

    private String FECHA_EMISION;
    private BigDecimal CLASE_DOCUMENTO;
    private String TIPO_DOCUMENTO;
    private String NUMERO_RESOLUCION;
    private String NUMERO_SERIE;
    private String NUMERO_INTERNO_DEL;
    private String NUMERO_INTERNO_AL;
    private String NUMERO_DOCUMENTO_DEL;
    private String NUMERO_DOCUMENTO_AL;
    private String NRO_MAQUINA_REGISTRADORA;
    private Double TOTAL_EXENTO;
    private Double TOTAL_EXENTO_NO_SUJETO;
    private Double VENTAS_NO_SUJETAS;
    private Double VENTAS_GRAVADAS_LOCALES;
    private Double EXPORTACIONES_CENTROAMERICA;
    private Double EXPORTACIONES_NO_CENTROAMERICA;
    private Double EXPORTACIONES_SERVICIOS;
    private Double VENTAS_ZONA_FRANCA;
    private Double CUENTA_TERCEROS_NO_DOMICILIADOS;
    private Double TOTAL_VENTAS;
    private BigDecimal ANEXO;

    public ReporteIvaConsumidorFinalDTO(String FECHA_EMISION, BigDecimal CLASE_DOCUMENTO, String TIPO_DOCUMENTO,
                                        String NUMERO_RESOLUCION, String NUMERO_SERIE, String NUMERO_INTERNO_DEL,  String NUMERO_INTERNO_AL,
                                        String NUMERO_DOCUMENTO_DEL, String NUMERO_DOCUMENTO_AL, String NRO_MAQUINA_REGISTRADORA,
                                        Double TOTAL_EXENTO, Double TOTAL_EXENTO_NO_SUJETO, Double VENTAS_NO_SUJETAS,
                                        Double VENTAS_GRAVADAS_LOCALES, Double EXPORTACIONES_CENTROAMERICA,
                                        Double EXPORTACIONES_NO_CENTROAMERICA, Double EXPORTACIONES_SERVICIOS,
                                        Double VENTAS_ZONA_FRANCA, Double CUENTA_TERCEROS_NO_DOMICILIADOS,
                                        Double TOTAL_VENTAS, BigDecimal ANEXO) {
        this.FECHA_EMISION = FECHA_EMISION;
        this.CLASE_DOCUMENTO = CLASE_DOCUMENTO;
        this.TIPO_DOCUMENTO = TIPO_DOCUMENTO;
        this.NUMERO_RESOLUCION = NUMERO_RESOLUCION;
        this.NUMERO_SERIE = NUMERO_SERIE;
        this.NUMERO_INTERNO_DEL = NUMERO_INTERNO_DEL;
        this.NUMERO_INTERNO_AL = NUMERO_INTERNO_AL;
        this.NUMERO_DOCUMENTO_DEL = NUMERO_DOCUMENTO_DEL;
        this.NUMERO_DOCUMENTO_AL = NUMERO_DOCUMENTO_AL;
        this.NRO_MAQUINA_REGISTRADORA = NRO_MAQUINA_REGISTRADORA;
        this.TOTAL_EXENTO = TOTAL_EXENTO;
        this.TOTAL_EXENTO_NO_SUJETO = TOTAL_EXENTO_NO_SUJETO;
        this.VENTAS_NO_SUJETAS = VENTAS_NO_SUJETAS;
        this.VENTAS_GRAVADAS_LOCALES = VENTAS_GRAVADAS_LOCALES;
        this.EXPORTACIONES_CENTROAMERICA = EXPORTACIONES_CENTROAMERICA;
        this.EXPORTACIONES_NO_CENTROAMERICA = EXPORTACIONES_NO_CENTROAMERICA;
        this.EXPORTACIONES_SERVICIOS = EXPORTACIONES_SERVICIOS;
        this.VENTAS_ZONA_FRANCA = VENTAS_ZONA_FRANCA;
        this.CUENTA_TERCEROS_NO_DOMICILIADOS = CUENTA_TERCEROS_NO_DOMICILIADOS;
        this.TOTAL_VENTAS = TOTAL_VENTAS;
        this.ANEXO = ANEXO;
    }

}
