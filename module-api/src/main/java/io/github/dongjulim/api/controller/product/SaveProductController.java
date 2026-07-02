package io.github.dongjulim.api.controller.product;

import io.github.dongjulim.api.service.FileStorageService;
import io.github.dongjulim.domain.product.dto.SaveProductRequest;
import io.github.dongjulim.domain.product.usecase.SaveProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SaveProductController {

    private final SaveProductUseCase saveProductUseCase;
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/api/v2/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveProduct(
            @RequestPart("data") @Valid SaveProductRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String imageUrl = (image != null && !image.isEmpty()) ? fileStorageService.store(image) : null;
        saveProductUseCase.saveProduct(request, imageUrl, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
