package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.product.dto.FindProductRequest;
import io.github.dongjulim.domain.product.dto.FindProductResponse;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.product.usecase.FindProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindProductService implements FindProductUseCase {

    private final ProductRepository productRepository;

    @Override
    public Page<FindProductResponse> findProduct(FindProductRequest request, Pageable pageable) {
        return productRepository.findAllBySearchCondition(request.getCategory(), request.getKeyword(), pageable)
                .map(FindProductResponse::from);
    }
}
