package com.anthonylldev.airsoftstore.service.mapper;

import com.anthonylldev.airsoftstore.domain.Category;
import com.anthonylldev.airsoftstore.domain.SubCategory;
import com.anthonylldev.airsoftstore.service.dto.CategoryDTO;
import com.anthonylldev.airsoftstore.service.dto.SubCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubCategory} and its DTO {@link SubCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubCategoryMapper extends EntityMapper<SubCategoryDTO, SubCategory> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryTitle")
    SubCategoryDTO toDto(SubCategory s);

    @Named("categoryTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    CategoryDTO toDtoCategoryTitle(Category category);
}
