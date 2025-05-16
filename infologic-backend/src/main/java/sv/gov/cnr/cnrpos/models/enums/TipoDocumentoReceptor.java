package sv.gov.cnr.cnrpos.models.enums;

public enum TipoDocumentoReceptor {
    NIT("36", "NIT"),
    DUI("13", "DUI"),
    OTRO("37", "Otro"),
    PASAPORTE("03", "Pasaporte"),
    CARNET_DE_RESIDENTE("02", "Carnet de Residente");

    private final String codigo;
    private final String valor;

    TipoDocumentoReceptor(String codigo, String valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getValor() {
        return valor;
    }
}
