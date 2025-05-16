package sv.gov.cnr.cnrpos.models.enums;

public enum TipoDTE {
    FACTURA("01", "Factura"),
    COMPROBANTE_CREDITO_FISCAL("03", "Comprobante de crédito fiscal"),
    NOTA_REMISION("04", "Nota de remisión"),
    NOTA_CREDITO("05", "Nota de crédito"),
    NOTA_DEBITO("06", "Nota de débito"),
    COMPROBANTE_RETENCION("07", "Comprobante de retención"),
    COMPROBANTE_LIQUIDACION("08", "Comprobante de liquidación"),
    DOCUMENTO_CONTABLE_LIQUIDACION("09", "Documento contable de liquidación"),
    FACTURAS_EXPORTACION("11", "Facturas de exportación"),
    FACTURA_SUJETO_EXCLUIDO("14", "Factura de sujeto excluido"),
    COMPROBANTE_DONACION("15", "Comprobante de donación");

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
}
