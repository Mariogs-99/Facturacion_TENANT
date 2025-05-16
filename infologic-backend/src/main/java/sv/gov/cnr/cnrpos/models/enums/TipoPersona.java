package sv.gov.cnr.cnrpos.models.enums;

public enum TipoPersona {
    PERSONA_NATURAL("Persona Natural"),
    PERSONA_JURIDICA("Persona Juridica");

    private final String descripcion;
    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
