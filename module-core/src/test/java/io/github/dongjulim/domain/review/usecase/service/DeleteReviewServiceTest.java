package io.github.dongjulim.domain.review.usecase.service;

import io.github.dongjulim.domain.common.exception.ReviewNotFoundException;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeleteReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private DeleteReviewService deleteReviewService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("deleteReview - deleteCheck가 true로 변경된다")
    void deleteReview_shouldSetDeleteCheckTrue() {
        Review review = Review.builder().id(1L).userId(1L).productId(10L).content("맛있어요").rating(4).deleteCheck(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(reviewRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(review));

        deleteReviewService.deleteReview(1L, "testuser");

        assertThat(review.getDeleteCheck()).isTrue();
    }

    @Test
    @DisplayName("deleteReview - 존재하지 않는 리뷰면 ReviewNotFoundException을 던진다")
    void deleteReview_throwsReviewNotFoundException_whenReviewNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(reviewRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deleteReviewService.deleteReview(99L, "testuser"))
                .isInstanceOf(ReviewNotFoundException.class);
    }

    @Test
    @DisplayName("deleteReview - 작성자가 아니면 ReviewNotFoundException을 던진다")
    void deleteReview_throwsReviewNotFoundException_whenNotOwner() {
        Review review = Review.builder().id(1L).userId(999L).productId(10L).content("맛있어요").rating(4).deleteCheck(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(reviewRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(review));

        assertThatThrownBy(() -> deleteReviewService.deleteReview(1L, "testuser"))
                .isInstanceOf(ReviewNotFoundException.class);
    }
}
