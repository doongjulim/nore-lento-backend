package io.github.dongjulim.api.controller.product;

import io.github.dongjulim.api.service.FileStorageService;
import io.github.dongjulim.domain.product.dto.UpdateProductRequest;
import io.github.dongjulim.domain.product.usecase.UpdateProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateProductController {

    private final UpdateProductUseCase updateProductUseCase;
    private final FileStorageService fileStorageService;

    @PutMapping(value = "/api/v2/product/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long id,
            @RequestPart("data") @Valid UpdateProductRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        String imageUrl = (image != null && !image.isEmpty()) ? fileStorageService.store(image) : null;
        updateProductUseCase.updateProduct(id, request, imageUrl);
        return ResponseEntity.ok().build();
    }
}
