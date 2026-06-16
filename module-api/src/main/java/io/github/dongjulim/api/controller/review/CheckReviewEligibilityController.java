package io.github.dongjulim.api.controller.review;

import io.github.dongjulim.domain.review.dto.CheckReviewEligibilityResponse;
import io.github.dongjulim.domain.review.usecase.CheckReviewEligibilityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckReviewEligibilityController {

    private final CheckReviewEligibilityUseCase checkReviewEligibilityUseCase;

    @GetMapping("/api/v2/reviews/eligible")
    public ResponseEntity<CheckReviewEligibilityResponse> checkEligibility(
            @RequestParam Long productId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(checkReviewEligibilityUseCase.checkEligibility(productId, userDetails.getUsername()));
    }
}
