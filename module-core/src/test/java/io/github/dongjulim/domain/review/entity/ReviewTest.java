package io.github.dongjulim.domain.review.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewTest {

    private Review createReview() {
        return Review.builder()
                .id(1L)
                .userId(1L)
                .productId(10L)
                .content("맛있어요")
                .rating(4)
                .deleteCheck(false)
                .build();
    }

    @Test
    @DisplayName("updateReview - 내용과 평점이 정상적으로 변경된다")
    void updateReview_shouldUpdateContentAndRating() {
        Review review = createReview();

        review.updateReview("정말 맛있어요", 5);

        assertThat(review.getContent()).isEqualTo("정말 맛있어요");
        assertThat(review.getRating()).isEqualTo(5);
    }

    @Test
    @DisplayName("updateReview - 내용 변경 시 userId는 유지된다")
    void updateReview_shouldNotChangeUserId() {
        Review review = createReview();

        review.updateReview("변경된 내용", 3);

        assertThat(review.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("deleteReview - deleteCheck가 true로 변경된다")
    void deleteReview_shouldSetDeleteCheckTrue() {
        Review review = createReview();

        review.deleteReview();

        assertThat(review.getDeleteCheck()).isTrue();
    }
}
