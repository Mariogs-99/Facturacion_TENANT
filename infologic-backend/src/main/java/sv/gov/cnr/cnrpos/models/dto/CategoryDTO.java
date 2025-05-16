package sv.gov.cnr.cnrpos.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {

    private Long idCatalogo;
    private String idMh;
    private String valor;
    private String alterno;
    private String alternoA;
    private String alternoB;
    private String grupo;
    private String subGrupo;
    private String grupoPadre;
    private String catPadre;
    private String idMhPadre;
    private Long idPadre;
    private String descripcion;

}
