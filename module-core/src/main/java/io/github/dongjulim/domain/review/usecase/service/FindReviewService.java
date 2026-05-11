package io.github.dongjulim.domain.review.usecase.service;

import io.github.dongjulim.domain.review.dto.FindReviewResponse;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import io.github.dongjulim.domain.review.usecase.FindReviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindReviewService implements FindReviewUseCase {

    private final ReviewRepository reviewRepository;

    @Override
    public Page<FindReviewResponse> findReviews(Long productId, Pageable pageable) {
        return reviewRepository.findAllByProductId(productId, pageable)
                .map(FindReviewResponse::from);
    }
}
