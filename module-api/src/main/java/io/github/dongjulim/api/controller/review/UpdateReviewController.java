package io.github.dongjulim.api.controller.review;

import io.github.dongjulim.domain.review.dto.UpdateReviewRequest;
import io.github.dongjulim.domain.review.usecase.UpdateReviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateReviewController {

    private final UpdateReviewUseCase updateReviewUseCase;

    @PutMapping("/api/v2/reviews/{id}")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long id,
            @RequestBody @Valid UpdateReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        updateReviewUseCase.updateReview(id, request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
