package io.github.dongjulim.api.controller.review;

import io.github.dongjulim.domain.review.usecase.DeleteReviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteReviewController {

    private final DeleteReviewUseCase deleteReviewUseCase;

    @DeleteMapping("/api/v2/reviews/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        deleteReviewUseCase.deleteReview(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
