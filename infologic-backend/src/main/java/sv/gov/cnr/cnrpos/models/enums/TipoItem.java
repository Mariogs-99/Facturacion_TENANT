package sv.gov.cnr.cnrpos.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoItem {
    BIENES(1, "Bienes"),
    SERVICIOS(2, "Servicios"),
    AMBOS(3, "Ambos (Bienes y Servicios)"),
    OTROS_TRIBUTOS(4, "Otros tributos por ítem");

    private final int codigo;
    private final String descripcion;

    TipoItem(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public int getCodigo() {
        return codigo;
    }
    @JsonValue
    public String getDescripcion() {
        return descripcion;
    }

    @JsonCreator
    public static TipoItem fromDescripcion(String descripcion) {
        System.out.println("TIPO ITEM DESCRIP : " + descripcion);
        for (TipoItem tipo : TipoItem.values()) {
            System.out.println("TIPO: " + tipo.getDescripcion());
            if (tipo.getDescripcion().equalsIgnoreCase(descripcion.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("TipoItem no encontrado para la cadena recibida: " + descripcion);
    }
}

