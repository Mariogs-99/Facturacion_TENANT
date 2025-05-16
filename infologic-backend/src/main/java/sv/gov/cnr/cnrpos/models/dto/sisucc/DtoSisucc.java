package sv.gov.cnr.cnrpos.models.dto.sisucc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtoSisucc {
    private String mensaje;
    DtoUsuario usuario;
    private int codigo;
}
