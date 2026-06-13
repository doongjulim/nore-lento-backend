package io.github.dongjulim.domain.point.dto;

import io.github.dongjulim.domain.point.entity.PointHistory;
import io.github.dongjulim.domain.point.enums.PointHistoryType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class FindPointHistoryResponse {

    private final Long id;
    private final Long amount;
    private final PointHistoryType type;
    private final LocalDateTime createdAt;

    public static FindPointHistoryResponse from(PointHistory history) {
        return new FindPointHistoryResponse(
                history.getId(),
                history.getAmount(),
                history.getType(),
                history.getCreateAt()
        );
    }
}
