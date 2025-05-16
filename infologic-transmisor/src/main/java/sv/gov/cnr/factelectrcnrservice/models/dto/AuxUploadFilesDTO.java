package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.Data;
import sv.gov.cnr.factelectrcnrservice.entities.CnrposUploadFile;

@Data
public class AuxUploadFilesDTO {

    private CnrposUploadFile cnrposUploadFile;
    private CnrposUploadFileResponseDTO cnrposUploadFileResponseDTO;
}
