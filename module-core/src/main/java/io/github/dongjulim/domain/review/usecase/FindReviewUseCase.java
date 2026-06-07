package io.github.dongjulim.domain.review.usecase;

import io.github.dongjulim.domain.review.dto.FindReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindReviewUseCase {
    Page<FindReviewResponse> findReviews(Long productId, Integer rating, Pageable pageable);
}
