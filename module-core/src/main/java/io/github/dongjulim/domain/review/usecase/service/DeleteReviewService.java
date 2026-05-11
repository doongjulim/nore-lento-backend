package io.github.dongjulim.domain.review.usecase.service;

import io.github.dongjulim.domain.common.exception.ReviewNotFoundException;
import io.github.dongjulim.domain.review.entity.Review;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import io.github.dongjulim.domain.review.usecase.DeleteReviewUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteReviewService implements DeleteReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final UserLoader userLoader;

    @Override
    public void deleteReview(Long id, String username) {
        User user = userLoader.load(username);

        Review review = reviewRepository.findByIdAndDeleteCheckFalse(id)
                .filter(r -> r.getUserId().equals(user.getId()))
                .orElseThrow(ReviewNotFoundException::new);

        review.deleteReview();
    }
}
