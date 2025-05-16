package sv.gov.cnr.cnrpos.models.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sv.gov.cnr.cnrpos.entities.RCatalogos;
import sv.gov.cnr.cnrpos.models.dto.CategoryDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T11:51:34-0600",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl extends CategoryMapper {

    @Override
    public RCatalogos toEntity(CategoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        RCatalogos rCatalogos = new RCatalogos();

        rCatalogos.setIdCatalogo( dto.getIdCatalogo() );
        rCatalogos.setIdMh( dto.getIdMh() );
        rCatalogos.setValor( dto.getValor() );
        rCatalogos.setAlterno( dto.getAlterno() );
        rCatalogos.setAlternoA( dto.getAlternoA() );
        rCatalogos.setAlternoB( dto.getAlternoB() );
        rCatalogos.setGrupo( dto.getGrupo() );
        rCatalogos.setSubGrupo( dto.getSubGrupo() );
        rCatalogos.setGrupoPadre( dto.getGrupoPadre() );
        rCatalogos.setCatPadre( dto.getCatPadre() );
        rCatalogos.setIdMhPadre( dto.getIdMhPadre() );
        rCatalogos.setIdPadre( dto.getIdPadre() );
        rCatalogos.setDescripcion( dto.getDescripcion() );

        return rCatalogos;
    }

    @Override
    public CategoryDTO toDto(RCatalogos target) {
        if ( target == null ) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setIdCatalogo( target.getIdCatalogo() );
        categoryDTO.setIdMh( target.getIdMh() );
        categoryDTO.setValor( target.getValor() );
        categoryDTO.setAlterno( target.getAlterno() );
        categoryDTO.setAlternoA( target.getAlternoA() );
        categoryDTO.setAlternoB( target.getAlternoB() );
        categoryDTO.setGrupo( target.getGrupo() );
        categoryDTO.setSubGrupo( target.getSubGrupo() );
        categoryDTO.setGrupoPadre( target.getGrupoPadre() );
        categoryDTO.setCatPadre( target.getCatPadre() );
        categoryDTO.setIdMhPadre( target.getIdMhPadre() );
        categoryDTO.setIdPadre( target.getIdPadre() );
        categoryDTO.setDescripcion( target.getDescripcion() );

        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> toDtoList(List<RCatalogos> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CategoryDTO> list = new ArrayList<CategoryDTO>( entities.size() );
        for ( RCatalogos rCatalogos : entities ) {
            list.add( toDto( rCatalogos ) );
        }

        return list;
    }
}
