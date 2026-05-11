package io.github.dongjulim.domain.review.usecase;

import io.github.dongjulim.domain.review.dto.SaveReviewRequest;

public interface SaveReviewUseCase {
    void saveReview(SaveReviewRequest request, String username);
}
