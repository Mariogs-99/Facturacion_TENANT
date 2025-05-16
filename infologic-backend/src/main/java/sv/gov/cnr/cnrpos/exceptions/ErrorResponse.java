package sv.gov.cnr.cnrpos.exceptions;

public class ErrorResponse {
    private final String mensaje;

    public ErrorResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
