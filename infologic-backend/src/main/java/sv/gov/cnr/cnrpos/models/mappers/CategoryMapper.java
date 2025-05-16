package sv.gov.cnr.cnrpos.models.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sv.gov.cnr.cnrpos.entities.RCatalogos;
import sv.gov.cnr.cnrpos.models.dto.CategoryDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {
    @Mapping(target = "idCatalogo", source = "idCatalogo")
    @Mapping(target = "idMh", source = "idMh")
    @Mapping(target = "valor", source = "valor")
    @Mapping(target = "alterno", source = "alterno")
    @Mapping(target = "alternoA", source = "alternoA")
    @Mapping(target = "alternoB", source = "alternoB")
    @Mapping(target = "grupo", source = "grupo")
    @Mapping(target = "subGrupo", source = "subGrupo")
    @Mapping(target = "grupoPadre", source = "grupoPadre")
    @Mapping(target = "catPadre", source = "catPadre")
    @Mapping(target = "idMhPadre", source = "idMhPadre")
    @Mapping(target = "idPadre", source = "idPadre")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "notas", ignore = true)
    @Mapping(target = "usuCrea", ignore = true)
    @Mapping(target = "fecCrea", ignore = true)
    @Mapping(target = "usuModifica", ignore = true)
    @Mapping(target = "fecModifica", ignore = true)
    @Mapping(target = "trash", ignore = true)
    public abstract RCatalogos toEntity(CategoryDTO dto);

    @InheritInverseConfiguration
    public abstract CategoryDTO toDto(RCatalogos target);
    public abstract List<CategoryDTO> toDtoList(List<RCatalogos> entities);
}

