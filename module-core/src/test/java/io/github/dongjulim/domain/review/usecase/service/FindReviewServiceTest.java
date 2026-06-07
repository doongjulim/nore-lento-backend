package io.github.dongjulim.domain.review.usecase.service;

import io.github.dongjulim.domain.review.dto.FindReviewResponse;
import io.github.dongjulim.domain.review.entity.Review;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private FindReviewService findReviewService;

    private Review createReview(Long id, Long userId, String username, String content, Integer rating) {
        User user = User.builder().id(userId).username(username).build();
        Review review = Review.builder()
                .id(id).userId(userId).productId(10L).content(content).rating(rating).deleteCheck(false).build();
        ReflectionTestUtils.setField(review, "user", user);
        return review;
    }

    @Test
    @DisplayName("findReviews - rating이 null이면 상품의 전체 리뷰 목록을 페이징하여 반환한다")
    void findReviews_shouldReturnPagedReviews() {
        Review review1 = createReview(1L, 1L, "user1", "맛있어요", 5);
        Review review2 = createReview(2L, 2L, "user2", "보통이에요", 3);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviewPage = new PageImpl<>(List.of(review1, review2), pageable, 2);

        given(reviewRepository.findAllByProductId(10L, null, pageable)).willReturn(reviewPage);

        Page<FindReviewResponse> result = findReviewService.findReviews(10L, null, pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getContent()).isEqualTo("맛있어요");
        assertThat(result.getContent().get(0).getRating()).isEqualTo(5);
        assertThat(result.getContent().get(0).getUsername()).isEqualTo("user1");
        assertThat(result.getContent().get(1).getContent()).isEqualTo("보통이에요");
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("findReviews - rating을 지정하면 해당 평점의 리뷰만 반환한다")
    void findReviews_shouldReturnFilteredReviews_whenRatingIsGiven() {
        Review review = createReview(1L, 1L, "user1", "맛있어요", 5);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviewPage = new PageImpl<>(List.of(review), pageable, 1);

        given(reviewRepository.findAllByProductId(10L, 5, pageable)).willReturn(reviewPage);

        Page<FindReviewResponse> result = findReviewService.findReviews(10L, 5, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getRating()).isEqualTo(5);
    }

    @Test
    @DisplayName("findReviews - 리뷰가 없으면 빈 페이지를 반환한다")
    void findReviews_shouldReturnEmptyPage_whenNoReviews() {
        Pageable pageable = PageRequest.of(0, 10);
        given(reviewRepository.findAllByProductId(10L, null, pageable)).willReturn(Page.empty(pageable));

        Page<FindReviewResponse> result = findReviewService.findReviews(10L, null, pageable);

        assertThat(result.getContent()).isEmpty();
    }
}
