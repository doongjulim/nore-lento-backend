package io.github.dongjulim.api.controller.productCategory;

import io.github.dongjulim.domain.productCategory.dto.FindProductCategoryResponse;
import io.github.dongjulim.domain.productCategory.usecase.FindProductCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindProductCategoryController {

    private final FindProductCategoryUseCase findProductCategoryUseCase;

    @GetMapping("/api/v2/product/categories")
    public ResponseEntity<List<FindProductCategoryResponse>> findProductCategories() {
        return ResponseEntity.ok(findProductCategoryUseCase.findProductCategories());
    }
}
