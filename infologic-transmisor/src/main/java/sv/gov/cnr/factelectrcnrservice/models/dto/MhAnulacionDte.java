package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MhAnulacionDte {

    String ambiente;
    Integer idEnvio;
    Integer version;
    String documento;

}
