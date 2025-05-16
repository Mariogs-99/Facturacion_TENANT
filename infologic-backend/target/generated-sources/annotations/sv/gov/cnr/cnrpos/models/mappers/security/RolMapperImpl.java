package sv.gov.cnr.cnrpos.models.mappers.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;
import sv.gov.cnr.cnrpos.models.dto.PermissionDTO;
import sv.gov.cnr.cnrpos.models.dto.RolDTO;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.models.security.Rol;
import sv.gov.cnr.cnrpos.models.security.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T11:51:34-0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class RolMapperImpl extends RolMapper {

    @Override
    public Rol toEntity(RolDTO source) {
        if ( source == null ) {
            return null;
        }

        Rol rol = new Rol();

        rol.setIdRole( source.getIdRole() );
        rol.setNameRole( source.getNameRole() );
        rol.setDescriptionRole( source.getDescriptionRole() );
        rol.setCreatedAt( source.getCreatedAt() );
        rol.setUpdatedAt( source.getUpdatedAt() );
        rol.setDeletedAt( source.getDeletedAt() );
        List<Long> list = source.getPermissionIds();
        if ( list != null ) {
            rol.setPermissionIds( new ArrayList<Long>( list ) );
        }
        Set<User> set = source.getUser();
        if ( set != null ) {
            rol.setUser( new HashSet<User>( set ) );
        }
        rol.setPermission( permissionDTOSetToPermissionSet( source.getPermission() ) );

        return rol;
    }

    @Override
    public RolDTO toDto(Rol target) {
        if ( target == null ) {
            return null;
        }

        RolDTO rolDTO = new RolDTO();

        rolDTO.setIdRole( target.getIdRole() );
        rolDTO.setNameRole( target.getNameRole() );
        rolDTO.setDescriptionRole( target.getDescriptionRole() );
        List<Long> list = target.getPermissionIds();
        if ( list != null ) {
            rolDTO.setPermissionIds( new ArrayList<Long>( list ) );
        }
        rolDTO.setPermission( permissionSetToPermissionDTOSet( target.getPermission() ) );

        return rolDTO;
    }

    @Override
    public PermissionDTO toDtoPermission(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionDTO permissionDTO = new PermissionDTO();

        permissionDTO.setIdPermissions( permission.getIdPermissions() );
        permissionDTO.setNamePermissions( permission.getNamePermissions() );
        permissionDTO.setDescriptionPermissions( permission.getDescriptionPermissions() );
        permissionDTO.setImagePermissions( permission.getImagePermissions() );
        permissionDTO.setMethodPermissions( permission.getMethodPermissions() );
        permissionDTO.setUriPermissions( permission.getUriPermissions() );
        permissionDTO.setSlugPermissions( permission.getSlugPermissions() );

        return permissionDTO;
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

    protected Permission permissionDTOToPermission(PermissionDTO permissionDTO) {
        if ( permissionDTO == null ) {
            return null;
        }

        Permission permission = new Permission();

        permission.setIdPermissions( permissionDTO.getIdPermissions() );
        permission.setNamePermissions( permissionDTO.getNamePermissions() );
        permission.setDescriptionPermissions( permissionDTO.getDescriptionPermissions() );
        permission.setImagePermissions( permissionDTO.getImagePermissions() );
        permission.setMethodPermissions( permissionDTO.getMethodPermissions() );
        permission.setUriPermissions( permissionDTO.getUriPermissions() );
        permission.setSlugPermissions( permissionDTO.getSlugPermissions() );
        permission.setCreatedAt( permissionDTO.getCreatedAt() );
        permission.setUpdatedAt( permissionDTO.getUpdatedAt() );
        permission.setDeletedAt( permissionDTO.getDeletedAt() );
        Set<Rol> set = permissionDTO.getRoles();
        if ( set != null ) {
            permission.setRoles( new HashSet<Rol>( set ) );
        }
        permission.setMenuItems( menuItemsDTOSetToMenuItemsSet( permissionDTO.getMenuItems() ) );

        return permission;
    }

    protected Set<Permission> permissionDTOSetToPermissionSet(Set<PermissionDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Permission> set1 = new HashSet<Permission>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( PermissionDTO permissionDTO : set ) {
            set1.add( permissionDTOToPermission( permissionDTO ) );
        }

        return set1;
    }

    protected Set<PermissionDTO> permissionSetToPermissionDTOSet(Set<Permission> set) {
        if ( set == null ) {
            return null;
        }

        Set<PermissionDTO> set1 = new HashSet<PermissionDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Permission permission : set ) {
            set1.add( toDtoPermission( permission ) );
        }

        return set1;
    }
}
