package io.github.dongjulim.domain.product.usecase;

import io.github.dongjulim.domain.product.dto.FindProductRequest;
import io.github.dongjulim.domain.product.dto.FindProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindProductUseCase {

    Page<FindProductResponse> findProduct(FindProductRequest request, Pageable pageable);
}
