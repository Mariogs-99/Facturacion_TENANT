package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnvioDteVariosDTO {
    String codigoGeneracion;
    String[] correo;
}
