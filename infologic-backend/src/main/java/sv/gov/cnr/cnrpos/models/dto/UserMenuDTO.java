package sv.gov.cnr.cnrpos.models.dto;

import lombok.Data;

@Data
public class UserMenuDTO {
    private Long idMenu;
    private Long idCategory;
    private String nameMenu;
    private String slugMenu;
    private CategoryDTO category;
}
