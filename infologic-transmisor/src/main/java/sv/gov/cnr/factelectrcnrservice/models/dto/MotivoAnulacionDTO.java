package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MotivoAnulacionDTO {

    Integer tipoInvalidacion;
    String motivoInvalidacion;
    String nombreResponsable;
    String tipoDocResponsable;
    String numDocResponsable;
    String nombreSolicita;
    String tipoDocSolicita;
    String numDocSolicita;
    String codigoGeneracionr;


}
