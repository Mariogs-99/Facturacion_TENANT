package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MhFirmadorRequest {

    private String nit;
    private String passwordPri;
    private Boolean activo;
    private Object dteJson;

}
