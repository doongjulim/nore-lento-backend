package io.github.dongjulim.domain.productCategory.usecase.service;

import io.github.dongjulim.domain.productCategory.dto.SaveProductCategoryRequest;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
import io.github.dongjulim.domain.productCategory.usecase.SaveProductCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveProductCategoryService implements SaveProductCategoryUseCase {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public void saveProductCategory(SaveProductCategoryRequest request) {
        ProductCategory category = ProductCategory.builder()
                .name(request.getName())
                .build();
        productCategoryRepository.save(category);
    }
}
