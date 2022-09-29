package com.anthonylldev.airsoftstore.service.mapper;

import com.anthonylldev.airsoftstore.domain.Brand;
import com.anthonylldev.airsoftstore.domain.Item;
import com.anthonylldev.airsoftstore.domain.SubCategory;
import com.anthonylldev.airsoftstore.service.dto.BrandDTO;
import com.anthonylldev.airsoftstore.service.dto.ItemDTO;
import com.anthonylldev.airsoftstore.service.dto.SubCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "brand", source = "brand", qualifiedByName = "brandTitle")
    @Mapping(target = "subCategory", source = "subCategory", qualifiedByName = "subCategoryTitle")
    ItemDTO toDto(Item s);

    @Override
    @Mapping(target = "brand", source = "brand", qualifiedByName = "brandEntityTitle")
    @Mapping(target = "subCategory", source = "subCategory", qualifiedByName = "subCategoryEntityTitle")
    Item toEntity(ItemDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "brand", source = "brand", qualifiedByName = "brandEntityTitle")
    @Mapping(target = "subCategory", source = "subCategory", qualifiedByName = "subCategoryEntityTitle")
    void partialUpdate(@MappingTarget Item entity, ItemDTO dto);

    @Named("brandTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    BrandDTO toDtoBrandTitle(Brand brand);

    @Named("brandEntityTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    Brand toDtoBrandTitle(BrandDTO brandDTO);

    @Named("subCategoryTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    SubCategoryDTO toDtoSubCategoryTitle(SubCategory subCategory);

    @Named("subCategoryEntityTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    SubCategory toDtoSubCategoryTitle(SubCategoryDTO subCategoryDTO);
}
