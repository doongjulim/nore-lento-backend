package io.github.dongjulim.domain.review.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.common.exception.ReviewNotEligibleException;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.review.dto.SaveReviewRequest;
import io.github.dongjulim.domain.review.entity.Review;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import io.github.dongjulim.domain.review.usecase.SaveReviewUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveReviewService implements SaveReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserLoader userLoader;

    @Override
    public void saveReview(SaveReviewRequest request, String username) {
        User user = userLoader.load(username);

        Product product = productRepository.findByIdAndDeleteCheckFalse(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        if (!orderItemRepository.existsCompletedOrderByUserIdAndProductId(user.getId(), product.getId())) {
            throw new ReviewNotEligibleException();
        }

        Review review = Review.builder()
                .userId(user.getId())
                .productId(product.getId())
                .content(request.getContent())
                .rating(request.getRating())
                .build();

        reviewRepository.save(review);
    }
}
