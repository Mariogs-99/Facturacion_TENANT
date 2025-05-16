package sv.gov.cnr.cnrpos.models.dto;

import lombok.Data;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItemsDTO {
    private Long idMenuItems;
    private Long idMenu;
    private String nameMenuItems;
    private String uriMenuItems;
    private String descriptionMenuItems;
    private String imageMenuItems;
    private String type;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

}
