package com.anthonylldev.airsoftstore.service.mapper;

import com.anthonylldev.airsoftstore.domain.Category;
import com.anthonylldev.airsoftstore.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {

    @Override
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "removeSubcategory", ignore = true)
    Category toEntity(CategoryDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "removeSubcategory", ignore = true)
    void partialUpdate(@MappingTarget Category entity, CategoryDTO dto);
}
