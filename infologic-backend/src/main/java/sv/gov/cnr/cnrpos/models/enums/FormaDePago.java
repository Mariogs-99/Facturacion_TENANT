package sv.gov.cnr.cnrpos.models.enums;

public enum FormaDePago {
    BILLETES_Y_MONEDAS("01", "Billetes y monedas"),
    TARJETA_DEBITO("02", "Tarjeta Débito"),
    TARJETA_CREDITO("03", "Tarjeta Crédito"),
    CHEQUE("04", "Cheque"),
    TRANSFERENCIA_DEPOSITO_BANCARIO("05", "Transferencia/Depósito Bancario"),
    VALES_O_CUPONES("06", "Vales o Cupones"),
    DINERO_ELECTRONICO("08", "Dinero electrónico"),
    MONEDERO_ELECTRONICO("09", "Monedero electrónico"),
    CERTIFICADO_O_TARJETA_DE_REGALO("10", "Certificado o tarjeta de regalo"),
    BITCOIN("11", "Bitcoin"),
    OTRAS_CRIPTOMONEDAS("12", "Otras Criptomonedas"),
    CUENTAS_POR_PAGAR_DEL_RECEPTOR("13", "Cuentas por pagar del receptor"),
    GIRO_BANCARIO("14", "Giro bancario"),
    OTROS("99", "Otros (se debe indicar el medio de pago)");

    private final String codigo;
    private final String descripcion;

    FormaDePago(String codigo, String descripcion) {
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

