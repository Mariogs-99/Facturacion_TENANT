package sv.gov.cnr.factelectrcnrservice.models.dto;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MhRecepcionDte {
    public String ambiente;
    public Integer idEnvio;
    public Integer version;
    public String tipoDte;
    public String documento;
    public String codigoGeneracion;
}
