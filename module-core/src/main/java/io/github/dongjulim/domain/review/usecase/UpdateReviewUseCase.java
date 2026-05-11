package io.github.dongjulim.domain.review.usecase;

import io.github.dongjulim.domain.review.dto.UpdateReviewRequest;

public interface UpdateReviewUseCase {
    void updateReview(Long id, UpdateReviewRequest request, String username);
}
