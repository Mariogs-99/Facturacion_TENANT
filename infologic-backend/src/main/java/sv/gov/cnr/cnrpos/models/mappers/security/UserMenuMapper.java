package sv.gov.cnr.cnrpos.models.mappers.security;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.models.dto.CategoryDTO;
import sv.gov.cnr.cnrpos.models.dto.UserMenuDTO;

@Mapper(componentModel = "spring")

public abstract class UserMenuMapper {
    public abstract Menu toEntity(UserMenuDTO source);

    public abstract UserMenuDTO toDto(Menu target);

    @Mapping(target = "idMenu", source = "idMenu")
    @Mapping(target = "nameMenu", source = "nameMenu")
    @Mapping(target = "slugMenu", source = "slugMenu")
    @Mapping(target = "category", source = "category.valor")
    public abstract UserMenuDTO toUserMenuDTO(Menu source);


    public CategoryDTO map(String value) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setValor(value);
        return categoryDTO;
    }
}
