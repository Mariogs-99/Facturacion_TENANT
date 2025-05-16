package sv.gov.cnr.factelectrcnrservice.models.dto;


public class ComprobantePagosDTO {
    private int codigo;
    private String mensaje;

    // Getters y setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
