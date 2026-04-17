package io.github.dongjulim.api.controller.product;

import io.github.dongjulim.domain.product.dto.FindProductDetailResponse;
import io.github.dongjulim.domain.product.usecase.FindProductDetailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindProductDetailController {

    private final FindProductDetailUseCase findProductDetailUseCase;

    @GetMapping("/api/v2/product/{id}")
    public ResponseEntity<FindProductDetailResponse> findProductDetail(@PathVariable Long id) {
        return ResponseEntity.ok(findProductDetailUseCase.findProductDetail(id));
    }
}
