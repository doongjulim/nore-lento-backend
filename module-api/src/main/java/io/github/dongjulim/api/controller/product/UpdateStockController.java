package io.github.dongjulim.api.controller.product;

import io.github.dongjulim.domain.stock.dto.UpdateStockRequest;
import io.github.dongjulim.domain.stock.usecase.UpdateStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateStockController {

    private final UpdateStockUseCase updateStockUseCase;

    @PatchMapping("/api/v2/product/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Void> updateStock(
            @PathVariable Long id,
            @RequestBody @Valid UpdateStockRequest request
    ) {
        updateStockUseCase.updateStock(id, request);
        return ResponseEntity.ok().build();
    }
}
