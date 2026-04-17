package io.github.dongjulim.api.controller.product;

import io.github.dongjulim.domain.product.dto.SaveProductRequest;
import io.github.dongjulim.domain.product.usecase.SaveProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SaveProductController {

    private final SaveProductUseCase saveProductUseCase;

    @PostMapping("/api/v2/product")
    public ResponseEntity<Void> saveProduct(
            @RequestBody @Valid SaveProductRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        saveProductUseCase.saveProduct(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
