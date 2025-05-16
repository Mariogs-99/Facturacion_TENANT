package sv.gov.cnr.cnrpos.models.mappers.security;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.context.annotation.Bean;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;
import sv.gov.cnr.cnrpos.models.dto.PermissionDTO;
import sv.gov.cnr.cnrpos.models.security.Permission;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PermisosMapper {
    public abstract Permission toEntity(PermissionDTO source);


    @Mapping(target = "idPermissions", source = "idPermissions")
    @Mapping(target = "namePermissions", source = "namePermissions")
    @Mapping(target = "descriptionPermissions", source = "descriptionPermissions")
    @Mapping(target = "createdAt", source = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", source = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", source = "deletedAt", ignore = true)
    @Mapping(target = "menuItems", qualifiedByName = "toMenuItemsDTO")
    public abstract PermissionDTO toDtoList(Permission target);

    @InheritInverseConfiguration
    @Mapping(target = "idMenuItems", source = "idMenuItems")
    @Mapping(target = "idMenu", ignore = true)
    @Mapping(target = "nameMenuItems", ignore = true)
    @Mapping(target = "uriMenuItems", source = "uriMenuItems")
    @Mapping(target = "descriptionMenuItems", source = "descriptionMenuItems", ignore = true)
    @Mapping(target = "imageMenuItems", source = "imageMenuItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Named("toMenuItemsDTO")
    public abstract MenuItemsDTO toMenuItemsDTO(MenuItems target);



    @Mapping(target = "idPermissions", source = "idPermissions")
    @Mapping(target = "namePermissions", source = "namePermissions")
    @Mapping(target = "descriptionPermissions", source = "descriptionPermissions")
    @Mapping(target = "roles", source = "roles", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", source = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", source = "deletedAt", ignore = true)
    @Mapping(target = "menuItems", qualifiedByName = "toMenuItemDTO")
    public abstract PermissionDTO toDto(Permission target);
    @InheritInverseConfiguration
    @Mapping(target = "idMenuItems", source = "idMenuItems")
    @Mapping(target = "idMenu", source = "idMenu")
    @Mapping(target = "nameMenuItems", source = "nameMenuItems")
    @Mapping(target = "uriMenuItems", source = "uriMenuItems", ignore = true)
    @Mapping(target = "descriptionMenuItems", source = "descriptionMenuItems", ignore = true)
    @Mapping(target = "imageMenuItems", source = "imageMenuItems", ignore = true)
//    @Mapping(target = "category", source = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Named("toMenuItemDTO")
    public abstract MenuItemsDTO toMenuItemDTO(MenuItems target);

}
