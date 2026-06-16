package io.github.dongjulim.domain.review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckReviewEligibilityResponse {
    private final boolean eligible;
}
