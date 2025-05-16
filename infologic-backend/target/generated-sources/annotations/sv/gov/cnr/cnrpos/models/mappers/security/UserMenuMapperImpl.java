package sv.gov.cnr.cnrpos.models.mappers.security;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sv.gov.cnr.cnrpos.entities.Menu;
import sv.gov.cnr.cnrpos.entities.RCatalogos;
import sv.gov.cnr.cnrpos.models.dto.CategoryDTO;
import sv.gov.cnr.cnrpos.models.dto.UserMenuDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T11:51:33-0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserMenuMapperImpl extends UserMenuMapper {

    @Override
    public Menu toEntity(UserMenuDTO source) {
        if ( source == null ) {
            return null;
        }

        Menu menu = new Menu();

        menu.setIdMenu( source.getIdMenu() );
        menu.setIdCategory( source.getIdCategory() );
        menu.setNameMenu( source.getNameMenu() );
        menu.setSlugMenu( source.getSlugMenu() );
        menu.setCategory( categoryDTOToRCatalogos( source.getCategory() ) );

        return menu;
    }

    @Override
    public UserMenuDTO toDto(Menu target) {
        if ( target == null ) {
            return null;
        }

        UserMenuDTO userMenuDTO = new UserMenuDTO();

        userMenuDTO.setIdMenu( target.getIdMenu() );
        userMenuDTO.setIdCategory( target.getIdCategory() );
        userMenuDTO.setNameMenu( target.getNameMenu() );
        userMenuDTO.setSlugMenu( target.getSlugMenu() );
        userMenuDTO.setCategory( rCatalogosToCategoryDTO( target.getCategory() ) );

        return userMenuDTO;
    }

    @Override
    public UserMenuDTO toUserMenuDTO(Menu source) {
        if ( source == null ) {
            return null;
        }

        UserMenuDTO userMenuDTO = new UserMenuDTO();

        userMenuDTO.setIdMenu( source.getIdMenu() );
        userMenuDTO.setNameMenu( source.getNameMenu() );
        userMenuDTO.setSlugMenu( source.getSlugMenu() );
        userMenuDTO.setCategory( map( sourceCategoryValor( source ) ) );
        userMenuDTO.setIdCategory( source.getIdCategory() );

        return userMenuDTO;
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

    private String sourceCategoryValor(Menu menu) {
        if ( menu == null ) {
            return null;
        }
        RCatalogos category = menu.getCategory();
        if ( category == null ) {
            return null;
        }
        String valor = category.getValor();
        if ( valor == null ) {
            return null;
        }
        return valor;
    }
}
