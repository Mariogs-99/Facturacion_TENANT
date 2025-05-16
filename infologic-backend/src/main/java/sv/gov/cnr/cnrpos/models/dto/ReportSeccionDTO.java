package sv.gov.cnr.cnrpos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ReportSeccionDTO {

    private String label;
    private List<Reporte> reportes;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Reporte {
        private String reporteName;
        private String descripcion;
    }
}
