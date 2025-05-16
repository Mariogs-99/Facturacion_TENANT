package sv.gov.cnr.cnrpos.models.dto;


import lombok.Data;

@Data
public class KpiDTO {

    private String id;
    private String name;
    private Long value;
    private Long target;
    private double trend;
    private String description;
}
