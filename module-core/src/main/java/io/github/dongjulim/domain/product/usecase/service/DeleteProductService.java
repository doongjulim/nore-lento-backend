package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.product.usecase.DeleteProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepository productRepository;

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(ProductNotFoundException::new);
        product.deleteProduct();
    }
}
