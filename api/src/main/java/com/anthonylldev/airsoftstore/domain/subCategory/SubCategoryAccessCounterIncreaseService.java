package com.anthonylldev.airsoftstore.domain.subCategory;

import com.anthonylldev.airsoftstore.service.criteria.ItemCriteria;
import org.springframework.stereotype.Service;

@Service
public interface SubCategoryAccessCounterIncreaseService {
    void increase(ItemCriteria criteria);
}
