package io.github.dongjulim.domain.review.usecase;

import io.github.dongjulim.domain.review.dto.CheckReviewEligibilityResponse;

public interface CheckReviewEligibilityUseCase {
    CheckReviewEligibilityResponse checkEligibility(Long productId, String username);
}
