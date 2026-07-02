package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductCategoryNotFoundException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.dto.UpdateProductRequest;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.product.usecase.UpdateProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public void updateProduct(Long id, UpdateProductRequest request, String imageUrl) {
        Product product = productRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(ProductNotFoundException::new);
        ProductCategory category = productCategoryRepository.findByIdAndDeleteCheckFalse(request.getCategoryId())
                .orElseThrow(ProductCategoryNotFoundException::new);
        product.updateProduct(request.getName(), request.getPrice(), request.getDescription(), category.getId());
        if (imageUrl != null) {
            product.updateImageUrl(imageUrl);
        }
    }
}
