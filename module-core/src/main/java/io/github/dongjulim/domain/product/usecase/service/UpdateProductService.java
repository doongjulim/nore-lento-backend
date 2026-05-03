package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.dto.ProductRequest;
import io.github.dongjulim.domain.product.entity.Product;
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

    @Override
    public void updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(ProductNotFoundException::new);
        product.updateProduct(request.getName(), request.getPrice(), request.getDescription(), request.getCategory());
    }
}
