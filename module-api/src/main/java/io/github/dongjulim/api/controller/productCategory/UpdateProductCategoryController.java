package io.github.dongjulim.api.controller.productCategory;

import io.github.dongjulim.domain.productCategory.dto.UpdateProductCategoryRequest;
import io.github.dongjulim.domain.productCategory.usecase.UpdateProductCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateProductCategoryController {

    private final UpdateProductCategoryUseCase updateProductCategoryUseCase;

    @PutMapping("/api/v2/product/categories/{id}")
    public ResponseEntity<Void> updateProductCategory(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductCategoryRequest request
    ) {
        updateProductCategoryUseCase.updateProductCategory(id, request);
        return ResponseEntity.ok().build();
    }
}
