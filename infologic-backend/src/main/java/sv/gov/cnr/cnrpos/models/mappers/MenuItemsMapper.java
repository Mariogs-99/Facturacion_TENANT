package sv.gov.cnr.cnrpos.models.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class MenuItemsMapper {

    @Mapping(target = "idMenuItems", source = "idMenuItems")
    @Mapping(target = "idMenu", source = "idMenu")
    @Mapping(target = "nameMenuItems", source = "nameMenuItems")
    @Mapping(target = "uriMenuItems", source = "uriMenuItems")
    @Mapping(target = "descriptionMenuItems", source = "descriptionMenuItems")
    @Mapping(target = "imageMenuItems", source = "imageMenuItems")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "deletedAt", source = "deletedAt")
    public abstract MenuItems toEntity(MenuItemsDTO source);

    @InheritInverseConfiguration
    public abstract MenuItemsDTO toDto(MenuItems target);

    public abstract Set<MenuItemsDTO> toDto(Set<MenuItems> menuItems);

}
