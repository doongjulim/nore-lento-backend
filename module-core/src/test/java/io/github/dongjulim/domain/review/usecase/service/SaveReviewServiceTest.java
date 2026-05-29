package io.github.dongjulim.domain.review.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.common.exception.ReviewNotEligibleException;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.review.dto.SaveReviewRequest;
import io.github.dongjulim.domain.review.entity.Review;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SaveReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private SaveReviewService saveReviewService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
        product = Product.builder().id(10L).name("사과").price(1000L).categoryId(1L).deleteCheck(false).build();
    }

    @Test
    @DisplayName("saveReview - 구매 완료한 상품에 리뷰가 정상적으로 저장된다")
    void saveReview_shouldSaveReview_whenUserHasPurchasedProduct() {
        SaveReviewRequest request = new SaveReviewRequest();
        ReflectionTestUtils.setField(request, "productId", 10L);
        ReflectionTestUtils.setField(request, "content", "맛있어요");
        ReflectionTestUtils.setField(request, "rating", 5);

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(orderItemRepository.existsCompletedOrderByUserIdAndProductId(1L, 10L)).willReturn(true);

        saveReviewService.saveReview(request, "testuser");

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        then(reviewRepository).should().save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(1L);
        assertThat(captor.getValue().getProductId()).isEqualTo(10L);
        assertThat(captor.getValue().getContent()).isEqualTo("맛있어요");
        assertThat(captor.getValue().getRating()).isEqualTo(5);
    }

    @Test
    @DisplayName("saveReview - 구매 이력이 없으면 ReviewNotEligibleException을 던진다")
    void saveReview_throwsReviewNotEligibleException_whenUserHasNotPurchasedProduct() {
        SaveReviewRequest request = new SaveReviewRequest();
        ReflectionTestUtils.setField(request, "productId", 10L);
        ReflectionTestUtils.setField(request, "content", "맛있어요");
        ReflectionTestUtils.setField(request, "rating", 5);

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(10L)).willReturn(Optional.of(product));
        given(orderItemRepository.existsCompletedOrderByUserIdAndProductId(1L, 10L)).willReturn(false);

        assertThatThrownBy(() -> saveReviewService.saveReview(request, "testuser"))
                .isInstanceOf(ReviewNotEligibleException.class);
    }

    @Test
    @DisplayName("saveReview - 존재하지 않는 상품이면 ProductNotFoundException을 던진다")
    void saveReview_throwsProductNotFoundException_whenProductNotFound() {
        SaveReviewRequest request = new SaveReviewRequest();
        ReflectionTestUtils.setField(request, "productId", 99L);
        ReflectionTestUtils.setField(request, "content", "맛있어요");
        ReflectionTestUtils.setField(request, "rating", 5);

        given(userLoader.load("testuser")).willReturn(user);
        given(productRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveReviewService.saveReview(request, "testuser"))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
