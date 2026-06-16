package io.github.dongjulim.domain.review.usecase.service;

import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.review.dto.CheckReviewEligibilityResponse;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CheckReviewEligibilityServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private CheckReviewEligibilityService checkReviewEligibilityService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("checkEligibility - 구매 완료 후 리뷰를 쓰지 않았으면 eligible=true를 반환한다")
    void checkEligibility_shouldReturnTrue_whenEligible() {
        given(userLoader.load("testuser")).willReturn(user);
        given(orderItemRepository.existsCompletedOrderByUserIdAndProductId(1L, 10L)).willReturn(true);
        given(reviewRepository.existsByUserIdAndProductIdAndDeleteCheckFalse(1L, 10L)).willReturn(false);

        CheckReviewEligibilityResponse response = checkReviewEligibilityService.checkEligibility(10L, "testuser");

        assertThat(response.isEligible()).isTrue();
    }

    @Test
    @DisplayName("checkEligibility - 구매 이력이 없으면 eligible=false를 반환한다")
    void checkEligibility_shouldReturnFalse_whenNotPurchased() {
        given(userLoader.load("testuser")).willReturn(user);
        given(orderItemRepository.existsCompletedOrderByUserIdAndProductId(1L, 10L)).willReturn(false);

        CheckReviewEligibilityResponse response = checkReviewEligibilityService.checkEligibility(10L, "testuser");

        assertThat(response.isEligible()).isFalse();
    }

    @Test
    @DisplayName("checkEligibility - 이미 리뷰를 작성했으면 eligible=false를 반환한다")
    void checkEligibility_shouldReturnFalse_whenAlreadyReviewed() {
        given(userLoader.load("testuser")).willReturn(user);
        given(orderItemRepository.existsCompletedOrderByUserIdAndProductId(1L, 10L)).willReturn(true);
        given(reviewRepository.existsByUserIdAndProductIdAndDeleteCheckFalse(1L, 10L)).willReturn(true);

        CheckReviewEligibilityResponse response = checkReviewEligibilityService.checkEligibility(10L, "testuser");

        assertThat(response.isEligible()).isFalse();
    }
}
