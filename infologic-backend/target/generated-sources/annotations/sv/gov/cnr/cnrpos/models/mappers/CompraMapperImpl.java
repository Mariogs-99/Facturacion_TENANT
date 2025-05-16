package sv.gov.cnr.cnrpos.models.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sv.gov.cnr.cnrpos.entities.Compra;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.models.dto.CompraDTO;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;
import sv.gov.cnr.cnrpos.models.dto.PermissionDTO;
import sv.gov.cnr.cnrpos.models.dto.RolDTO;
import sv.gov.cnr.cnrpos.models.dto.UserDTO;
import sv.gov.cnr.cnrpos.models.security.Permission;
import sv.gov.cnr.cnrpos.models.security.Rol;
import sv.gov.cnr.cnrpos.models.security.User;
import sv.gov.cnr.cnrpos.models.security.User.UserBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T11:51:34-0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class CompraMapperImpl extends CompraMapper {

    @Override
    public Compra toEntity(CompraDTO source) {
        if ( source == null ) {
            return null;
        }

        Compra compra = new Compra();

        compra.setIdCompra( source.getIdCompra() );
        compra.setCodigoGeneracion( source.getCodigoGeneracion() );
        compra.setNumeroControl( source.getNumeroControl() );
        compra.setFechaEmision( source.getFechaEmision() );
        compra.setTipoOperacion( source.getTipoOperacion() );
        compra.setTipoDocumento( source.getTipoDocumento() );
        compra.setSelloRecepcion( source.getSelloRecepcion() );
        compra.setIdProveedor( source.getIdProveedor() );
        compra.setTotalGravado( source.getTotalGravado() );
        compra.setTotalExento( source.getTotalExento() );
        compra.setTotalOperacion( source.getTotalOperacion() );
        compra.setUser( userDTOToUser( source.getUser() ) );
        compra.setSucursal( source.getSucursal() );
        compra.setCreatedAt( source.getCreatedAt() );
        compra.setUpdatedAt( source.getUpdatedAt() );
        compra.setDeletedAt( source.getDeletedAt() );

        return compra;
    }

    @Override
    public CompraDTO toDtoList(Compra target) {
        if ( target == null ) {
            return null;
        }

        CompraDTO compraDTO = new CompraDTO();

        compraDTO.setIdCompra( target.getIdCompra() );
        compraDTO.setCodigoGeneracion( target.getCodigoGeneracion() );
        compraDTO.setNumeroControl( target.getNumeroControl() );
        compraDTO.setFechaEmision( target.getFechaEmision() );
        compraDTO.setTipoOperacion( target.getTipoOperacion() );
        compraDTO.setSelloRecepcion( target.getSelloRecepcion() );
        compraDTO.setIdProveedor( target.getIdProveedor() );
        compraDTO.setTotalGravado( target.getTotalGravado() );
        compraDTO.setTotalExento( target.getTotalExento() );
        compraDTO.setTotalOperacion( target.getTotalOperacion() );
        compraDTO.setTipoDocumento( target.getTipoDocumento() );

        return compraDTO;
    }

    @Override
    public CompraDTO toDto(Compra target) {
        if ( target == null ) {
            return null;
        }

        CompraDTO compraDTO = new CompraDTO();

        compraDTO.setIdCompra( target.getIdCompra() );
        compraDTO.setCodigoGeneracion( target.getCodigoGeneracion() );
        compraDTO.setNumeroControl( target.getNumeroControl() );
        compraDTO.setFechaEmision( target.getFechaEmision() );
        compraDTO.setTipoOperacion( target.getTipoOperacion() );
        compraDTO.setSelloRecepcion( target.getSelloRecepcion() );
        compraDTO.setIdProveedor( target.getIdProveedor() );
        compraDTO.setTotalGravado( target.getTotalGravado() );
        compraDTO.setTotalExento( target.getTotalExento() );
        compraDTO.setTotalOperacion( target.getTotalOperacion() );
        compraDTO.setUser( userToUserDTO( target.getUser() ) );
        compraDTO.setSucursal( target.getSucursal() );
        compraDTO.setTipoDocumento( target.getTipoDocumento() );

        return compraDTO;
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

    protected Rol rolDTOToRol(RolDTO rolDTO) {
        if ( rolDTO == null ) {
            return null;
        }

        Rol rol = new Rol();

        rol.setIdRole( rolDTO.getIdRole() );
        rol.setNameRole( rolDTO.getNameRole() );
        rol.setDescriptionRole( rolDTO.getDescriptionRole() );
        rol.setCreatedAt( rolDTO.getCreatedAt() );
        rol.setUpdatedAt( rolDTO.getUpdatedAt() );
        rol.setDeletedAt( rolDTO.getDeletedAt() );
        List<Long> list = rolDTO.getPermissionIds();
        if ( list != null ) {
            rol.setPermissionIds( new ArrayList<Long>( list ) );
        }
        Set<User> set = rolDTO.getUser();
        if ( set != null ) {
            rol.setUser( new HashSet<User>( set ) );
        }
        rol.setPermission( permissionDTOSetToPermissionSet( rolDTO.getPermission() ) );

        return rol;
    }

    protected Set<Rol> rolDTOSetToRolSet(Set<RolDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Rol> set1 = new HashSet<Rol>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( RolDTO rolDTO : set ) {
            set1.add( rolDTOToRol( rolDTO ) );
        }

        return set1;
    }

    protected User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.idUser( userDTO.getIdUser() );
        user.firstname( userDTO.getFirstname() );
        user.lastname( userDTO.getLastname() );
        user.email( userDTO.getEmail() );
        user.docType( userDTO.getDocType() );
        user.docNumber( userDTO.getDocNumber() );
        user.password( userDTO.getPassword() );
        user.phone( userDTO.getPhone() );
        user.isActive( userDTO.getIsActive() );
        user.testMode( userDTO.getTestMode() );
        user.idCompany( userDTO.getIdCompany() );
        user.idBranch( userDTO.getIdBranch() );
        user.idPosition( userDTO.getIdPosition() );
        user.carnet( userDTO.getCarnet() );
        user.usuario( userDTO.getUsuario() );
        user.tipo( userDTO.getTipo() );
        List<Long> list = userDTO.getRolIds();
        if ( list != null ) {
            user.rolIds( new ArrayList<Long>( list ) );
        }
        user.roles( rolDTOSetToRolSet( userDTO.getRoles() ) );

        return user.build();
    }

    protected MenuItemsDTO menuItemsToMenuItemsDTO(MenuItems menuItems) {
        if ( menuItems == null ) {
            return null;
        }

        MenuItemsDTO menuItemsDTO = new MenuItemsDTO();

        menuItemsDTO.setIdMenuItems( menuItems.getIdMenuItems() );
        menuItemsDTO.setIdMenu( menuItems.getIdMenu() );
        menuItemsDTO.setNameMenuItems( menuItems.getNameMenuItems() );
        menuItemsDTO.setUriMenuItems( menuItems.getUriMenuItems() );
        menuItemsDTO.setDescriptionMenuItems( menuItems.getDescriptionMenuItems() );
        menuItemsDTO.setImageMenuItems( menuItems.getImageMenuItems() );
        menuItemsDTO.setType( menuItems.getType() );
        menuItemsDTO.setCreatedAt( menuItems.getCreatedAt() );
        menuItemsDTO.setUpdatedAt( menuItems.getUpdatedAt() );
        menuItemsDTO.setDeletedAt( menuItems.getDeletedAt() );

        return menuItemsDTO;
    }

    protected Set<MenuItemsDTO> menuItemsSetToMenuItemsDTOSet(Set<MenuItems> set) {
        if ( set == null ) {
            return null;
        }

        Set<MenuItemsDTO> set1 = new HashSet<MenuItemsDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( MenuItems menuItems : set ) {
            set1.add( menuItemsToMenuItemsDTO( menuItems ) );
        }

        return set1;
    }

    protected PermissionDTO permissionToPermissionDTO(Permission permission) {
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
        permissionDTO.setCreatedAt( permission.getCreatedAt() );
        permissionDTO.setUpdatedAt( permission.getUpdatedAt() );
        permissionDTO.setDeletedAt( permission.getDeletedAt() );
        permissionDTO.setMenuItems( menuItemsSetToMenuItemsDTOSet( permission.getMenuItems() ) );
        Set<Rol> set1 = permission.getRoles();
        if ( set1 != null ) {
            permissionDTO.setRoles( new HashSet<Rol>( set1 ) );
        }

        return permissionDTO;
    }

    protected Set<PermissionDTO> permissionSetToPermissionDTOSet(Set<Permission> set) {
        if ( set == null ) {
            return null;
        }

        Set<PermissionDTO> set1 = new HashSet<PermissionDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Permission permission : set ) {
            set1.add( permissionToPermissionDTO( permission ) );
        }

        return set1;
    }

    protected RolDTO rolToRolDTO(Rol rol) {
        if ( rol == null ) {
            return null;
        }

        RolDTO rolDTO = new RolDTO();

        rolDTO.setIdRole( rol.getIdRole() );
        rolDTO.setNameRole( rol.getNameRole() );
        rolDTO.setDescriptionRole( rol.getDescriptionRole() );
        rolDTO.setCreatedAt( rol.getCreatedAt() );
        rolDTO.setUpdatedAt( rol.getUpdatedAt() );
        rolDTO.setDeletedAt( rol.getDeletedAt() );
        List<Long> list = rol.getPermissionIds();
        if ( list != null ) {
            rolDTO.setPermissionIds( new ArrayList<Long>( list ) );
        }
        Set<User> set = rol.getUser();
        if ( set != null ) {
            rolDTO.setUser( new HashSet<User>( set ) );
        }
        rolDTO.setPermission( permissionSetToPermissionDTOSet( rol.getPermission() ) );

        return rolDTO;
    }

    protected Set<RolDTO> rolSetToRolDTOSet(Set<Rol> set) {
        if ( set == null ) {
            return null;
        }

        Set<RolDTO> set1 = new HashSet<RolDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Rol rol : set ) {
            set1.add( rolToRolDTO( rol ) );
        }

        return set1;
    }

    protected UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setIdUser( user.getIdUser() );
        userDTO.setFirstname( user.getFirstname() );
        userDTO.setLastname( user.getLastname() );
        userDTO.setCarnet( user.getCarnet() );
        userDTO.setUsuario( user.getUsuario() );
        userDTO.setTipo( user.getTipo() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setDocType( user.getDocType() );
        userDTO.setDocNumber( user.getDocNumber() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setPhone( user.getPhone() );
        userDTO.setIsActive( user.getIsActive() );
        userDTO.setTestMode( user.getTestMode() );
        userDTO.setIdCompany( user.getIdCompany() );
        userDTO.setIdBranch( user.getIdBranch() );
        userDTO.setIdPosition( user.getIdPosition() );
        List<Long> list = user.getRolIds();
        if ( list != null ) {
            userDTO.setRolIds( new ArrayList<Long>( list ) );
        }
        userDTO.setRoles( rolSetToRolDTOSet( user.getRoles() ) );

        return userDTO;
    }
}
