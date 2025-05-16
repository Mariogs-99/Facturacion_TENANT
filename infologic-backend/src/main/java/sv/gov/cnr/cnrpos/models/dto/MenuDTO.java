package sv.gov.cnr.cnrpos.models.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class MenuDTO {
    private Long idMenu;
    private Long idCategory;
    private String nameMenu;
    private String descriptionMenu;
    private String slugMenu;
    private String imageMenu;
    private List<MenuItemsDTO> menuItems = new ArrayList<>();
    private CategoryDTO category;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
