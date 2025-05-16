package sv.gov.cnr.cnrpos.models.mappers;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sv.gov.cnr.cnrpos.entities.MenuItems;
import sv.gov.cnr.cnrpos.models.dto.MenuItemsDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T11:51:34-0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class MenuItemsMapperImpl extends MenuItemsMapper {

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
        menuItemsDTO.setIdMenu( target.getIdMenu() );
        menuItemsDTO.setNameMenuItems( target.getNameMenuItems() );
        menuItemsDTO.setUriMenuItems( target.getUriMenuItems() );
        menuItemsDTO.setDescriptionMenuItems( target.getDescriptionMenuItems() );
        menuItemsDTO.setImageMenuItems( target.getImageMenuItems() );
        menuItemsDTO.setType( target.getType() );
        menuItemsDTO.setCreatedAt( target.getCreatedAt() );
        menuItemsDTO.setUpdatedAt( target.getUpdatedAt() );
        menuItemsDTO.setDeletedAt( target.getDeletedAt() );

        return menuItemsDTO;
    }

    @Override
    public Set<MenuItemsDTO> toDto(Set<MenuItems> menuItems) {
        if ( menuItems == null ) {
            return null;
        }

        Set<MenuItemsDTO> set = new HashSet<MenuItemsDTO>( Math.max( (int) ( menuItems.size() / .75f ) + 1, 16 ) );
        for ( MenuItems menuItems1 : menuItems ) {
            set.add( toDto( menuItems1 ) );
        }

        return set;
    }
}
