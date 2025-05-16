package sv.gov.cnr.cnrpos.models.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.entities.RCatalogos;
import sv.gov.cnr.cnrpos.models.dto.CategoryDTO;
import sv.gov.cnr.cnrpos.models.dto.MenuDTO;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T11:51:34-0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class MenuMapperImpl extends MenuMapper {

    @Override
    public MenuItemsDTO toMenuItemsDTO(MenuItems source) {
        if ( source == null ) {
            return null;
        }

        MenuItemsDTO menuItemsDTO = new MenuItemsDTO();

        menuItemsDTO.setNameMenuItems( source.getNameMenuItems() );
        menuItemsDTO.setUriMenuItems( source.getUriMenuItems() );
        menuItemsDTO.setDescriptionMenuItems( source.getDescriptionMenuItems() );
        menuItemsDTO.setImageMenuItems( source.getImageMenuItems() );
        menuItemsDTO.setType( source.getType() );

        return menuItemsDTO;
    }

    @Override
    public List<MenuDTO> toDtoList(List<Menu> sourceList) {
        if ( sourceList == null ) {
            return null;
        }

        List<MenuDTO> list = new ArrayList<MenuDTO>( sourceList.size() );
        for ( Menu menu : sourceList ) {
            list.add( toDto( menu ) );
        }

        return list;
    }

    @Override
    public Menu toEntity(MenuDTO source) {
        if ( source == null ) {
            return null;
        }

        Menu menu = new Menu();

        menu.setIdMenu( source.getIdMenu() );
        menu.setIdCategory( source.getIdCategory() );
        menu.setNameMenu( source.getNameMenu() );
        menu.setDescriptionMenu( source.getDescriptionMenu() );
        menu.setSlugMenu( source.getSlugMenu() );
        menu.setImageMenu( source.getImageMenu() );
        menu.setCreatedAt( source.getCreatedAt() );
        menu.setUpdatedAt( source.getUpdatedAt() );
        menu.setDeletedAt( source.getDeletedAt() );
        menu.setCategory( categoryDTOToRCatalogos( source.getCategory() ) );
        menu.setMenuItems( menuItemsDTOListToMenuItemsSet( source.getMenuItems() ) );

        return menu;
    }

    @Override
    public MenuDTO toDto(Menu target) {
        if ( target == null ) {
            return null;
        }

        MenuDTO menuDTO = new MenuDTO();

        menuDTO.setCategory( rCatalogosToCategoryDTO( target.getCategory() ) );
        menuDTO.setIdMenu( target.getIdMenu() );
        menuDTO.setIdCategory( target.getIdCategory() );
        menuDTO.setNameMenu( target.getNameMenu() );
        menuDTO.setDescriptionMenu( target.getDescriptionMenu() );
        menuDTO.setSlugMenu( target.getSlugMenu() );
        menuDTO.setImageMenu( target.getImageMenu() );
        menuDTO.setMenuItems( menuItemsSetToMenuItemsDTOList( target.getMenuItems() ) );
        menuDTO.setCreatedAt( target.getCreatedAt() );
        menuDTO.setUpdatedAt( target.getUpdatedAt() );
        menuDTO.setDeletedAt( target.getDeletedAt() );

        return menuDTO;
    }

    @Override
    public MenuItems toEntity(MenuItemsDTO source) {
        if ( source == null ) {
            return null;
        }

        MenuItems menuItems = new MenuItems();

        menuItems.setIdMenuItems( source.getIdMenuItems() );
        menuItems.setIdMenu( source.getIdMenu() );
        menuItems.setNameMenuItems( source.getNameMenuItems() );
        menuItems.setUriMenuItems( source.getUriMenuItems() );
        menuItems.setDescriptionMenuItems( source.getDescriptionMenuItems() );
        menuItems.setImageMenuItems( source.getImageMenuItems() );
        menuItems.setType( source.getType() );
        menuItems.setCreatedAt( source.getCreatedAt() );
        menuItems.setUpdatedAt( source.getUpdatedAt() );
        menuItems.setDeletedAt( source.getDeletedAt() );

        return menuItems;
    }

    @Override
    public MenuItemsDTO toDto(MenuItems target) {
        if ( target == null ) {
            return null;
        }

        MenuItemsDTO menuItemsDTO = new MenuItemsDTO();

        menuItemsDTO.setIdMenuItems( target.getIdMenuItems() );
        menuItemsDTO.setNameMenuItems( target.getNameMenuItems() );
        menuItemsDTO.setUriMenuItems( target.getUriMenuItems() );
        menuItemsDTO.setDescriptionMenuItems( target.getDescriptionMenuItems() );
        menuItemsDTO.setImageMenuItems( target.getImageMenuItems() );
        menuItemsDTO.setType( target.getType() );
        menuItemsDTO.setCreatedAt( target.getCreatedAt() );
        menuItemsDTO.setUpdatedAt( target.getUpdatedAt() );
        menuItemsDTO.setDeletedAt( target.getDeletedAt() );
        menuItemsDTO.setIdMenu( target.getIdMenu() );

        return menuItemsDTO;
    }

    @Override
    public List<Menu> toEntityList(List<MenuDTO> targetList) {
        if ( targetList == null ) {
            return null;
        }

        List<Menu> list = new ArrayList<Menu>( targetList.size() );
        for ( MenuDTO menuDTO : targetList ) {
            list.add( toEntity( menuDTO ) );
        }

        return list;
    }

    @Override
    public MenuDTO toDtoMenuUserList(Menu target) {
        if ( target == null ) {
            return null;
        }

        MenuDTO menuDTO = new MenuDTO();

        menuDTO.setMenuItems( menuItemsSetToMenuItemsDTOList1( target.getMenuItems() ) );
        menuDTO.setNameMenu( target.getNameMenu() );
        menuDTO.setSlugMenu( target.getSlugMenu() );
        menuDTO.setImageMenu( target.getImageMenu() );
        menuDTO.setCategory( rCatalogosToCategoryDTO1( target.getCategory() ) );

        return menuDTO;
    }

    protected RCatalogos categoryDTOToRCatalogos(CategoryDTO categoryDTO) {
        if ( categoryDTO == null ) {
            return null;
        }

        RCatalogos rCatalogos = new RCatalogos();

        rCatalogos.setIdCatalogo( categoryDTO.getIdCatalogo() );
        rCatalogos.setIdMh( categoryDTO.getIdMh() );
        rCatalogos.setValor( categoryDTO.getValor() );
        rCatalogos.setAlterno( categoryDTO.getAlterno() );
        rCatalogos.setAlternoA( categoryDTO.getAlternoA() );
        rCatalogos.setAlternoB( categoryDTO.getAlternoB() );
        rCatalogos.setGrupo( categoryDTO.getGrupo() );
        rCatalogos.setSubGrupo( categoryDTO.getSubGrupo() );
        rCatalogos.setGrupoPadre( categoryDTO.getGrupoPadre() );
        rCatalogos.setCatPadre( categoryDTO.getCatPadre() );
        rCatalogos.setIdMhPadre( categoryDTO.getIdMhPadre() );
        rCatalogos.setIdPadre( categoryDTO.getIdPadre() );
        rCatalogos.setDescripcion( categoryDTO.getDescripcion() );

        return rCatalogos;
    }

    protected Set<MenuItems> menuItemsDTOListToMenuItemsSet(List<MenuItemsDTO> list) {
        if ( list == null ) {
            return null;
        }

        Set<MenuItems> set = new HashSet<MenuItems>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( MenuItemsDTO menuItemsDTO : list ) {
            set.add( toEntity( menuItemsDTO ) );
        }

        return set;
    }

    protected CategoryDTO rCatalogosToCategoryDTO(RCatalogos rCatalogos) {
        if ( rCatalogos == null ) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setIdCatalogo( rCatalogos.getIdCatalogo() );
        categoryDTO.setIdMh( rCatalogos.getIdMh() );
        categoryDTO.setValor( rCatalogos.getValor() );
        categoryDTO.setAlterno( rCatalogos.getAlterno() );
        categoryDTO.setAlternoA( rCatalogos.getAlternoA() );
        categoryDTO.setAlternoB( rCatalogos.getAlternoB() );
        categoryDTO.setGrupo( rCatalogos.getGrupo() );
        categoryDTO.setSubGrupo( rCatalogos.getSubGrupo() );
        categoryDTO.setGrupoPadre( rCatalogos.getGrupoPadre() );
        categoryDTO.setCatPadre( rCatalogos.getCatPadre() );
        categoryDTO.setIdMhPadre( rCatalogos.getIdMhPadre() );
        categoryDTO.setIdPadre( rCatalogos.getIdPadre() );
        categoryDTO.setDescripcion( rCatalogos.getDescripcion() );

        return categoryDTO;
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

    protected List<MenuItemsDTO> menuItemsSetToMenuItemsDTOList(Set<MenuItems> set) {
        if ( set == null ) {
            return null;
        }

        List<MenuItemsDTO> list = new ArrayList<MenuItemsDTO>( set.size() );
        for ( MenuItems menuItems : set ) {
            list.add( menuItemsToMenuItemsDTO( menuItems ) );
        }

        return list;
    }

    protected List<MenuItemsDTO> menuItemsSetToMenuItemsDTOList1(Set<MenuItems> set) {
        if ( set == null ) {
            return null;
        }

        List<MenuItemsDTO> list = new ArrayList<MenuItemsDTO>( set.size() );
        for ( MenuItems menuItems : set ) {
            list.add( toMenuItemsDTO( menuItems ) );
        }

        return list;
    }

    protected CategoryDTO rCatalogosToCategoryDTO1(RCatalogos rCatalogos) {
        if ( rCatalogos == null ) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setIdMh( rCatalogos.getIdMh() );
        categoryDTO.setValor( rCatalogos.getValor() );
        categoryDTO.setAlterno( rCatalogos.getAlterno() );
        categoryDTO.setAlternoA( rCatalogos.getAlternoA() );
        categoryDTO.setAlternoB( rCatalogos.getAlternoB() );
        categoryDTO.setGrupo( rCatalogos.getGrupo() );
        categoryDTO.setSubGrupo( rCatalogos.getSubGrupo() );
        categoryDTO.setGrupoPadre( rCatalogos.getGrupoPadre() );
        categoryDTO.setCatPadre( rCatalogos.getCatPadre() );
        categoryDTO.setIdMhPadre( rCatalogos.getIdMhPadre() );
        categoryDTO.setIdPadre( rCatalogos.getIdPadre() );
        categoryDTO.setDescripcion( rCatalogos.getDescripcion() );

        return categoryDTO;
    }
}
