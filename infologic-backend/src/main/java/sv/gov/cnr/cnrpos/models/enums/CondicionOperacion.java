package sv.gov.cnr.cnrpos.models.enums;

import lombok.Data;

public enum CondicionOperacion {
    CONTADO(1,"Contado"),
    CREDITO(2,"A Credito"),
    OTRO(3,"Otro");
    private final Integer codigo;
    private final String valor;

    CondicionOperacion(Integer codigo, String valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getValor() {
        return valor;
    }
}
