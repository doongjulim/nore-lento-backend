package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.dto.FindProductDetailResponse;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.product.usecase.FindProductDetailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindProductDetailService implements FindProductDetailUseCase {

    private final ProductRepository productRepository;

    @Override
    public FindProductDetailResponse findProductDetail(Long id) {
        Product product = productRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(ProductNotFoundException::new);
        return FindProductDetailResponse.from(product);
    }
}
