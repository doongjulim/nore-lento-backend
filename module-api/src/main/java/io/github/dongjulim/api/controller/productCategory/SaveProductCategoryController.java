package io.github.dongjulim.api.controller.productCategory;

import io.github.dongjulim.domain.productCategory.dto.SaveProductCategoryRequest;
import io.github.dongjulim.domain.productCategory.usecase.SaveProductCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SaveProductCategoryController {

    private final SaveProductCategoryUseCase saveProductCategoryUseCase;

    @PostMapping("/api/v2/product/categories")
    public ResponseEntity<Void> saveProductCategory(
            @RequestBody @Valid SaveProductCategoryRequest request
    ) {
        saveProductCategoryUseCase.saveProductCategory(request);
        return ResponseEntity.ok().build();
    }
}
