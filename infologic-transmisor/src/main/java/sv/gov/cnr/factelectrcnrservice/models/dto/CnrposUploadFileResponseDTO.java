package sv.gov.cnr.factelectrcnrservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CnrposUploadFileResponseDTO {
    private int code;
    private String mensaje;

    @Override
    public String toString() {
        return String.format("{ \"code\": %d, \"mensaje\": \"%s\" }", code, mensaje);
    }
}