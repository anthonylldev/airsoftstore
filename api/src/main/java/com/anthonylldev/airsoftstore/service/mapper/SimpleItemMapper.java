package com.anthonylldev.airsoftstore.service.mapper;

import com.anthonylldev.airsoftstore.domain.Item;
import com.anthonylldev.airsoftstore.service.dto.ItemDTO;
import com.anthonylldev.airsoftstore.service.dto.SimpleItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface SimpleItemMapper extends EntityMapper<SimpleItemDTO, Item> {

    @Override
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    @Mapping(target = "inclusionDate", ignore = true)
    Item toEntity(SimpleItemDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    @Mapping(target = "inclusionDate", ignore = true)
    void partialUpdate(@MappingTarget Item entity, SimpleItemDTO dto);
}
