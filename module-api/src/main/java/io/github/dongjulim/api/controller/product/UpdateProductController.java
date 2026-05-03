package io.github.dongjulim.api.controller.product;

import io.github.dongjulim.domain.product.dto.UpdateProductRequest;
import io.github.dongjulim.domain.product.usecase.UpdateProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateProductController {

    private final UpdateProductUseCase updateProductUseCase;

    @PutMapping("/api/v2/product/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductRequest request
    ) {
        updateProductUseCase.updateProduct(id, request);
        return ResponseEntity.ok().build();
    }
}
