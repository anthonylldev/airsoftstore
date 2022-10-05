package com.anthonylldev.airsoftstore.domain.subCategory.impl;

import com.anthonylldev.airsoftstore.domain.SubCategory;
import com.anthonylldev.airsoftstore.domain.subCategory.SubCategoryAccessCounterIncreaseService;
import com.anthonylldev.airsoftstore.repository.SubCategoryRepository;
import com.anthonylldev.airsoftstore.security.AuthoritiesConstants;
import com.anthonylldev.airsoftstore.security.SecurityUtils;
import com.anthonylldev.airsoftstore.service.ItemQueryService;
import com.anthonylldev.airsoftstore.service.criteria.ItemCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.LongFilter;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoryAccessCounterIncreaseServiceImpl implements SubCategoryAccessCounterIncreaseService {

    private final Logger log = LoggerFactory.getLogger(ItemQueryService.class);

    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryAccessCounterIncreaseServiceImpl(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    @Override
    public void increase(ItemCriteria criteria) {
        boolean currentUserIsUser = SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER);

        if (currentUserIsUser) {
            LongFilter subCategoriesLongFilter = criteria.getSubCategoryId();

            if (subCategoriesLongFilter != null) {
                for (Long subCategoryId :
                    subCategoriesLongFilter.getIn()) {

                    log.debug("findOne: {}", subCategoryId);

                    Optional<SubCategory> subCategory =  this.subCategoryRepository.findById(subCategoryId);

                    boolean increaseSuccessfully =  subCategory.map(_subCategory -> {
                        try {
                            _subCategory.setAccessCount(_subCategory.getAccessCount() + 1);
                        } catch (NullPointerException e) {
                            log.debug("subCategory: {}, have null in access_count.", subCategoryId);
                            return false;
                        }
                        _subCategory = this.subCategoryRepository.save(_subCategory);

                        return true;
                    }).orElse(false);

                    log.debug("increaseSuccessfully : {}", increaseSuccessfully);
                }
            }
        }
    }
}
