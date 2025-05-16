package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.Data;

@Data
public class EspecificoCP {

    private int codigo;
    private String mensaje;
    private String cp;
    private String digitado;
    private String actual;
}
