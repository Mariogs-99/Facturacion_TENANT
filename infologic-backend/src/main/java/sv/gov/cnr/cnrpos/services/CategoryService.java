package sv.gov.cnr.cnrpos.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sv.gov.cnr.cnrpos.entities.RCatalogos;
import sv.gov.cnr.cnrpos.models.dto.CategoryDTO;
import sv.gov.cnr.cnrpos.models.mappers.CategoryMapper;
import sv.gov.cnr.cnrpos.repositories.CategoryRespository;

import java.util.List;

@Service
@AllArgsConstructor

public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRespository categoryRespository;

    public List<RCatalogos> getCategory(String grupo) {
        return categoryRespository.findByGrupo(grupo);
    }

    public List<CategoryDTO> getCategoryDto(String grupo) {
        return categoryMapper.toDtoList(getCategory(grupo));
    }

    public List<CategoryDTO> getCategoryDtoIn(List<Long> ids) {
        return categoryMapper.toDtoList(categoryRespository.findByIds(ids));
    }
}
