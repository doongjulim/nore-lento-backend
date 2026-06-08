package io.github.dongjulim.api.controller.product;

import io.github.dongjulim.domain.stock.dto.FindStockResponse;
import io.github.dongjulim.domain.stock.usecase.FindStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindStockController {

    private final FindStockUseCase findStockUseCase;

    @GetMapping("/api/v2/product/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<FindStockResponse> findStock(@PathVariable Long id) {
        return ResponseEntity.ok(findStockUseCase.findStock(id));
    }
}
