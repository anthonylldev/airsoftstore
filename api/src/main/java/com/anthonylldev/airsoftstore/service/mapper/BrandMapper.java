package com.anthonylldev.airsoftstore.service.mapper;

import com.anthonylldev.airsoftstore.domain.Brand;
import com.anthonylldev.airsoftstore.service.dto.BrandDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Brand} and its DTO {@link BrandDTO}.
 */
@Mapper(componentModel = "spring")
public interface BrandMapper extends EntityMapper<BrandDTO, Brand> {

    @Override
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "removeItem", ignore = true)
    Brand toEntity(BrandDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "removeItem", ignore = true)
    void partialUpdate(@MappingTarget Brand entity, BrandDTO dto);
}
