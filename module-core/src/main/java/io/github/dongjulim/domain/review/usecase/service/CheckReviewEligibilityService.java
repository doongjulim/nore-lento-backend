package io.github.dongjulim.domain.review.usecase.service;

import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.review.dto.CheckReviewEligibilityResponse;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import io.github.dongjulim.domain.review.usecase.CheckReviewEligibilityUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckReviewEligibilityService implements CheckReviewEligibilityUseCase {

    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final UserLoader userLoader;

    @Override
    public CheckReviewEligibilityResponse checkEligibility(Long productId, String username) {
        User user = userLoader.load(username);

        boolean hasPurchased = orderItemRepository.existsCompletedOrderByUserIdAndProductId(user.getId(), productId);
        if (!hasPurchased) {
            return new CheckReviewEligibilityResponse(false);
        }

        boolean alreadyReviewed = reviewRepository.existsByUserIdAndProductIdAndDeleteCheckFalse(user.getId(), productId);
        return new CheckReviewEligibilityResponse(!alreadyReviewed);
    }
}
