package sv.gov.cnr.cnrpos.models.mappers.security;


import org.mapstruct.*;
import sv.gov.cnr.cnrpos.models.dto.PermissionDTO;
import sv.gov.cnr.cnrpos.models.dto.RolDTO;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.models.security.Rol;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class RolMapper {

    public abstract Rol toEntity(RolDTO source);
    @Mapping(target = "idRole", source = "idRole")
    @Mapping(target = "nameRole", source = "nameRole")
    @Mapping(target = "descriptionRole", source = "descriptionRole")
    @Mapping(target = "permissionIds", source = "permissionIds")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "permission",qualifiedByName = "toDtoPermission")
    public abstract RolDTO toDto(Rol target);
    @InheritInverseConfiguration
    @Mapping(target = "idPermissions", source = "idPermissions")
    @Mapping(target = "namePermissions", source = "namePermissions")
    @Mapping(target = "descriptionPermissions", source = "descriptionPermissions")
    @Mapping(target = "roles", source = "roles", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", source = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", source = "deletedAt", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    @Named("toDtoPermission")
    public abstract PermissionDTO toDtoPermission(Permission permission);




}

