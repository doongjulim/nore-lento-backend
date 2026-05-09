package io.github.dongjulim.domain.productCategory.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductCategoryNotFoundException;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
import io.github.dongjulim.domain.productCategory.usecase.DeleteProductCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteProductCategoryService implements DeleteProductCategoryUseCase {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public void deleteProductCategory(Long id) {
        ProductCategory category = productCategoryRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(ProductCategoryNotFoundException::new);
        category.delete();
    }
}
