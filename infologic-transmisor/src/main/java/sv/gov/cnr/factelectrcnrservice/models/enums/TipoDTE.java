package sv.gov.cnr.factelectrcnrservice.models.enums;

import java.util.EnumSet;
import java.util.Set;


public enum TipoDTE {
    FACTURA("01", "Factura"),
    COMPROBANTE_CREDITO_FISCAL("03", "Comprobante de crédito fiscal"),
    NOTA_REMISION("04", "Nota de remisión"),
    NOTA_CREDITO("05", "Nota de crédito"),
    NOTA_DEBITO("06", "Nota de débito"),
    COMPROBANTE_RETENCION("07", "Comprobante de retención"),
    COMPROBANTE_RETENCION2("007", "Comprobante de retención"),
    COMPROBANTE_LIQUIDACION("08", "Comprobante de liquidación"),
    DOCUMENTO_CONTABLE_LIQUIDACION("09", "Documento contable de liquidación"),
    FACTURAS_EXPORTACION("11", "Facturas de exportación"),
    FACTURA_SUJETO_EXCLUIDO("14", "Factura de sujeto excluido"),
    COMPROBANTE_DONACION("15", "Comprobante de donación"),

    CONTINGENCIA("998", "Documento de contingencia"),
    INVALIDACION("999", "Documento de anulación");

    private final String codigo;
    private final String descripcion;
    TipoDTE(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public String getCodigo() {
        return codigo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    // Método para obtener TipoDTE a partir del código
    public static TipoDTE obtenerPorCodigo(String codigo) {
        for (TipoDTE tipoDTE : values()) {
            if (tipoDTE.getCodigo().equals(codigo)) {
                return tipoDTE;
            }
        }
        throw new IllegalArgumentException("Código no válido: " + codigo);
    }

    //Método para obtener TipoDTE válido para evento contingencia
    public static boolean isTipoDTEContigencia(TipoDTE tipoDTE){
        Set<TipoDTE> allowedContingencia = EnumSet.of(TipoDTE.FACTURA,
                TipoDTE.COMPROBANTE_CREDITO_FISCAL,
                TipoDTE.NOTA_REMISION,
                TipoDTE.NOTA_CREDITO,
                TipoDTE.NOTA_DEBITO,
                TipoDTE.FACTURAS_EXPORTACION,
                TipoDTE.FACTURA_SUJETO_EXCLUIDO);
        try{
            return allowedContingencia.contains(tipoDTE);
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
