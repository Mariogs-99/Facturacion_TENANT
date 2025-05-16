package sv.gov.cnr.factelectrcnrservice.models.enums;

public enum VersionTipoDTE {

    FACTURA("01", 1),
    COMPROBANTE_CREDITO_FISCAL("03", 3),
    NOTA_REMISION("04", 3),
    NOTA_CREDITO("05", 3),
    NOTA_DEBITO("06", 3),
    COMPROBANTE_RETENCION("07", 1),
    COMPROBANTE_LIQUIDACION("08", 1),
    DOCUMENTO_CONTABLE_LIQUIDACION("09", 1),
    FACTURAS_EXPORTACION("11", 1),
    FACTURA_SUJETO_EXCLUIDO("14", 1),
    COMPROBANTE_DONACION("15", 1);

    private final String codigo;
    private final Integer version;
    VersionTipoDTE(String codigo, Integer version) {
        this.codigo = codigo;
        this.version = version;
    }
    public String getCodigo() {
        return codigo;
    }
    public Integer getVersion() {
        return version;
    }
    // Método para obtener TipoDTE a partir del código
    public static VersionTipoDTE obtenerPorCodigo(String codigo) {
        for (VersionTipoDTE versiontipoDTE : values()) {
            if (versiontipoDTE.getCodigo().equals(codigo)) {
                return versiontipoDTE;
            }
        }
        throw new IllegalArgumentException("Código no válido: " + codigo);
    }

    public static int obtenerVersionPorCodigo(String codigo) {
        return obtenerPorCodigo(codigo).getVersion();
    }





}
