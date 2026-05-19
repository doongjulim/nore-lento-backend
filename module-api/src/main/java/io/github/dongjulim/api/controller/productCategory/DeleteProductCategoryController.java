package io.github.dongjulim.api.controller.productCategory;

import io.github.dongjulim.domain.productCategory.usecase.DeleteProductCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteProductCategoryController {

    private final DeleteProductCategoryUseCase deleteProductCategoryUseCase;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @DeleteMapping("/api/v2/product/categories/{id}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long id) {
        deleteProductCategoryUseCase.deleteProductCategory(id);
        return ResponseEntity.ok().build();
    }
}
