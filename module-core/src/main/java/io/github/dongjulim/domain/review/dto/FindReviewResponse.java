package io.github.dongjulim.domain.review.dto;

import io.github.dongjulim.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindReviewResponse {

    private final Long id;
    private final Long userId;
    private final String username;
    private final Long productId;
    private final String content;
    private final Integer rating;
    private final LocalDateTime createAt;

    public static FindReviewResponse from(Review review) {
        return FindReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .username(review.getUser().getUsername())
                .productId(review.getProductId())
                .content(review.getContent())
                .rating(review.getRating())
                .createAt(review.getCreateAt())
                .build();
    }
}
