package sv.gov.cnr.cnrpos.models.mappers.security;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;
import sv.gov.cnr.cnrpos.models.dto.PermissionDTO;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.models.security.Rol;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T11:51:34-0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class PermisosMapperImpl extends PermisosMapper {

    @Override
    public Permission toEntity(PermissionDTO source) {
        if ( source == null ) {
            return null;
        }

        Permission permission = new Permission();

        permission.setIdPermissions( source.getIdPermissions() );
        permission.setNamePermissions( source.getNamePermissions() );
        permission.setDescriptionPermissions( source.getDescriptionPermissions() );
        permission.setImagePermissions( source.getImagePermissions() );
        permission.setMethodPermissions( source.getMethodPermissions() );
        permission.setUriPermissions( source.getUriPermissions() );
        permission.setSlugPermissions( source.getSlugPermissions() );
        permission.setCreatedAt( source.getCreatedAt() );
        permission.setUpdatedAt( source.getUpdatedAt() );
        permission.setDeletedAt( source.getDeletedAt() );
        Set<Rol> set = source.getRoles();
        if ( set != null ) {
            permission.setRoles( new HashSet<Rol>( set ) );
        }
        permission.setMenuItems( menuItemsDTOSetToMenuItemsSet( source.getMenuItems() ) );

        return permission;
    }

    @Override
    public PermissionDTO toDtoList(Permission target) {
        if ( target == null ) {
            return null;
        }

        PermissionDTO permissionDTO = new PermissionDTO();

        permissionDTO.setIdPermissions( target.getIdPermissions() );
        permissionDTO.setNamePermissions( target.getNamePermissions() );
        permissionDTO.setDescriptionPermissions( target.getDescriptionPermissions() );
        permissionDTO.setMenuItems( menuItemsSetToMenuItemsDTOSet( target.getMenuItems() ) );
        permissionDTO.setImagePermissions( target.getImagePermissions() );
        permissionDTO.setMethodPermissions( target.getMethodPermissions() );
        permissionDTO.setUriPermissions( target.getUriPermissions() );
        permissionDTO.setSlugPermissions( target.getSlugPermissions() );
        Set<Rol> set1 = target.getRoles();
        if ( set1 != null ) {
            permissionDTO.setRoles( new HashSet<Rol>( set1 ) );
        }

        return permissionDTO;
    }

    @Override
    public MenuItemsDTO toMenuItemsDTO(MenuItems target) {
        if ( target == null ) {
            return null;
        }

        MenuItemsDTO menuItemsDTO = new MenuItemsDTO();

        menuItemsDTO.setIdMenuItems( target.getIdMenuItems() );
        menuItemsDTO.setUriMenuItems( target.getUriMenuItems() );
        menuItemsDTO.setType( target.getType() );

        return menuItemsDTO;
    }

    @Override
    public PermissionDTO toDto(Permission target) {
        if ( target == null ) {
            return null;
        }

        PermissionDTO permissionDTO = new PermissionDTO();

        permissionDTO.setIdPermissions( target.getIdPermissions() );
        permissionDTO.setNamePermissions( target.getNamePermissions() );
        permissionDTO.setDescriptionPermissions( target.getDescriptionPermissions() );
        permissionDTO.setMenuItems( menuItemsSetToMenuItemsDTOSet1( target.getMenuItems() ) );
        permissionDTO.setImagePermissions( target.getImagePermissions() );
        permissionDTO.setMethodPermissions( target.getMethodPermissions() );
        permissionDTO.setUriPermissions( target.getUriPermissions() );
        permissionDTO.setSlugPermissions( target.getSlugPermissions() );

        return permissionDTO;
    }

    @Override
    public MenuItemsDTO toMenuItemDTO(MenuItems target) {
        if ( target == null ) {
            return null;
        }

        MenuItemsDTO menuItemsDTO = new MenuItemsDTO();

        menuItemsDTO.setIdMenuItems( target.getIdMenuItems() );
        menuItemsDTO.setIdMenu( target.getIdMenu() );
        menuItemsDTO.setNameMenuItems( target.getNameMenuItems() );
        menuItemsDTO.setType( target.getType() );

        return menuItemsDTO;
    }

    protected MenuItems menuItemsDTOToMenuItems(MenuItemsDTO menuItemsDTO) {
        if ( menuItemsDTO == null ) {
            return null;
        }

        MenuItems menuItems = new MenuItems();

        menuItems.setIdMenuItems( menuItemsDTO.getIdMenuItems() );
        menuItems.setIdMenu( menuItemsDTO.getIdMenu() );
        menuItems.setNameMenuItems( menuItemsDTO.getNameMenuItems() );
        menuItems.setUriMenuItems( menuItemsDTO.getUriMenuItems() );
        menuItems.setDescriptionMenuItems( menuItemsDTO.getDescriptionMenuItems() );
        menuItems.setImageMenuItems( menuItemsDTO.getImageMenuItems() );
        menuItems.setType( menuItemsDTO.getType() );
        menuItems.setCreatedAt( menuItemsDTO.getCreatedAt() );
        menuItems.setUpdatedAt( menuItemsDTO.getUpdatedAt() );
        menuItems.setDeletedAt( menuItemsDTO.getDeletedAt() );

        return menuItems;
    }

    protected Set<MenuItems> menuItemsDTOSetToMenuItemsSet(Set<MenuItemsDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<MenuItems> set1 = new HashSet<MenuItems>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( MenuItemsDTO menuItemsDTO : set ) {
            set1.add( menuItemsDTOToMenuItems( menuItemsDTO ) );
        }

        return set1;
    }

    protected Set<MenuItemsDTO> menuItemsSetToMenuItemsDTOSet(Set<MenuItems> set) {
        if ( set == null ) {
            return null;
        }

        Set<MenuItemsDTO> set1 = new HashSet<MenuItemsDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( MenuItems menuItems : set ) {
            set1.add( toMenuItemsDTO( menuItems ) );
        }

        return set1;
    }

    protected Set<MenuItemsDTO> menuItemsSetToMenuItemsDTOSet1(Set<MenuItems> set) {
        if ( set == null ) {
            return null;
        }

        Set<MenuItemsDTO> set1 = new HashSet<MenuItemsDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( MenuItems menuItems : set ) {
            set1.add( toMenuItemDTO( menuItems ) );
        }

        return set1;
    }
}
