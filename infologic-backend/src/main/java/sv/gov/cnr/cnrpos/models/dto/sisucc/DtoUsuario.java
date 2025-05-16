package sv.gov.cnr.cnrpos.models.dto.sisucc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtoUsuario {
    private float fila;
    private String codSis;
    private String nombreSistema;
    private String carnet;
    private String usuario;
    private String empDui;
    private String empNombreCnr;
    private String usuClave;
    private float tipo;
}