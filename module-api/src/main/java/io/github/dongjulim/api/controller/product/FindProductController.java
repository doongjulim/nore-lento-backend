package io.github.dongjulim.api.controller.product;

import io.github.dongjulim.domain.product.dto.FindProductRequest;
import io.github.dongjulim.domain.product.dto.FindProductResponse;
import io.github.dongjulim.domain.product.usecase.FindProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/product")
public class FindProductController {

    private final FindProductUseCase findProductUseCase;

    @GetMapping
    public ResponseEntity<Page<FindProductResponse>> findProducts(
            @ModelAttribute FindProductRequest request,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(findProductUseCase.findProduct(request, pageable));
    }
}
