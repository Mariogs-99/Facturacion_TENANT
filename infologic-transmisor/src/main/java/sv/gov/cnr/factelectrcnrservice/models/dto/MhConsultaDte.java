package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MhConsultaDte {
    private String nitEmisor;
    private String tdte;
    private String codigoGeneracion;
}
