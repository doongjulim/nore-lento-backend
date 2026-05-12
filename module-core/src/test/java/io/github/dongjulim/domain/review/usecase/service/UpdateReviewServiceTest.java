package io.github.dongjulim.domain.review.usecase.service;

import io.github.dongjulim.domain.common.exception.ReviewNotFoundException;
import io.github.dongjulim.domain.review.dto.UpdateReviewRequest;
import io.github.dongjulim.domain.review.entity.Review;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private UpdateReviewService updateReviewService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("updateReview - 내용과 평점이 정상적으로 변경된다")
    void updateReview_shouldUpdateReview() {
        Review review = Review.builder().id(1L).userId(1L).productId(10L).content("맛있어요").rating(4).deleteCheck(false).build();
        UpdateReviewRequest request = new UpdateReviewRequest();
        ReflectionTestUtils.setField(request, "content", "정말 맛있어요");
        ReflectionTestUtils.setField(request, "rating", 5);

        given(userLoader.load("testuser")).willReturn(user);
        given(reviewRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(review));

        updateReviewService.updateReview(1L, request, "testuser");

        assertThat(review.getContent()).isEqualTo("정말 맛있어요");
        assertThat(review.getRating()).isEqualTo(5);
    }

    @Test
    @DisplayName("updateReview - 존재하지 않는 리뷰면 ReviewNotFoundException을 던진다")
    void updateReview_throwsReviewNotFoundException_whenReviewNotFound() {
        UpdateReviewRequest request = new UpdateReviewRequest();
        ReflectionTestUtils.setField(request, "content", "변경된 내용");
        ReflectionTestUtils.setField(request, "rating", 3);

        given(userLoader.load("testuser")).willReturn(user);
        given(reviewRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateReviewService.updateReview(99L, request, "testuser"))
                .isInstanceOf(ReviewNotFoundException.class);
    }

    @Test
    @DisplayName("updateReview - 작성자가 아니면 ReviewNotFoundException을 던진다")
    void updateReview_throwsReviewNotFoundException_whenNotOwner() {
        Review review = Review.builder().id(1L).userId(999L).productId(10L).content("맛있어요").rating(4).deleteCheck(false).build();
        UpdateReviewRequest request = new UpdateReviewRequest();
        ReflectionTestUtils.setField(request, "content", "변경된 내용");
        ReflectionTestUtils.setField(request, "rating", 3);

        given(userLoader.load("testuser")).willReturn(user);
        given(reviewRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(review));

        assertThatThrownBy(() -> updateReviewService.updateReview(1L, request, "testuser"))
                .isInstanceOf(ReviewNotFoundException.class);
    }
}
