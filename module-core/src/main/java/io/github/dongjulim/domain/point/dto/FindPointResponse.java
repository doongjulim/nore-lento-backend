package io.github.dongjulim.domain.point.dto;

import io.github.dongjulim.domain.point.entity.UserPoint;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindPointResponse {

    private final Long balance;

    public static FindPointResponse from(UserPoint userPoint) {
        return new FindPointResponse(userPoint.getBalance());
    }

    public static FindPointResponse empty() {
        return new FindPointResponse(0L);
    }
}
