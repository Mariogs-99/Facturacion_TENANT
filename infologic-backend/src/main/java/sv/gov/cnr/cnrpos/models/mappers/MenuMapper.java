package sv.gov.cnr.cnrpos.models.mappers;


import jdk.jfr.Name;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.models.dto.CategoryDTO;
import sv.gov.cnr.cnrpos.models.dto.MenuDTO;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class MenuMapper {

    @Mapping(target = "idMenuItems", ignore = true)
    @Mapping(target = "idMenu", ignore = true)
    @Mapping(source = "nameMenuItems", target = "nameMenuItems")
    @Mapping(target = "uriMenuItems", source = "uriMenuItems")
    @Mapping(target = "descriptionMenuItems", source = "descriptionMenuItems")
    @Mapping(target = "imageMenuItems", source = "imageMenuItems")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Named("toMenuItemsDTO")
    public abstract MenuItemsDTO toMenuItemsDTO(MenuItems source);


    @Mapping(target = "nameMenuItems", ignore = true)
    @Mapping(target = "uriMenuItems", ignore = true)
    @Mapping(target = "imageMenuItems", ignore = true)
    @Mapping(target = "menuItems", qualifiedByName = "toMenuItemsDTO")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Name("toDtoList")
    public abstract List<MenuDTO> toDtoList(List<Menu> sourceList);

    public abstract Menu toEntity(MenuDTO source);

    @Mapping(target = "idMenu", source = "idMenu")
    @Mapping(target = "idCategory", source = "idCategory")
    @Mapping(target = "nameMenu", source = "nameMenu")
    @Mapping(target = "descriptionMenu", source = "descriptionMenu")
    @Mapping(target = "slugMenu", source = "slugMenu")
    @Mapping(target = "imageMenu", source = "imageMenu")
    @Mapping(target = "menuItems", source = "menuItems")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "deletedAt", source = "deletedAt")
    @Mapping(target = "category.idCatalogo", ignore = false)
    public abstract MenuDTO toDto(Menu target);

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
    @Mapping(target = "idMenuItems", source = "idMenuItems")
    @Mapping(target = "nameMenuItems", source = "nameMenuItems", ignore = false)
    @Mapping(target = "uriMenuItems", source = "uriMenuItems")
    @Mapping(target = "descriptionMenuItems", source = "descriptionMenuItems")
    @Mapping(target = "imageMenuItems", source = "imageMenuItems")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "deletedAt", source = "deletedAt")
    @Named("toDto")
    public abstract MenuItemsDTO toDto(MenuItems target);



    public abstract List<Menu> toEntityList(List<MenuDTO> targetList);




    @Mapping(target = "idMenu", ignore = true)
    @Mapping(target = "idCategory", ignore = true)
    @Mapping(target = "descriptionMenu", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "category.idCatalogo", ignore = true)
    @Mapping(target = "menuItems", qualifiedByName = "toMenuItemsDTO")
    @Named("toDtoMenuUserList")
    public abstract MenuDTO toDtoMenuUserList(Menu target);


}

