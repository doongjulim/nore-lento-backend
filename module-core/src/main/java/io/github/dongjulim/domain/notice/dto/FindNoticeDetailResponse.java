package io.github.dongjulim.domain.notice.dto;

import io.github.dongjulim.domain.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindNoticeDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final long likeCount;
    private final LocalDateTime createAt;
    private final String createId;
    private final LocalDateTime updateAt;
    private final String updateId;

    public static FindNoticeDetailResponse from(Notice notice, long likeCount) {
        return FindNoticeDetailResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .likeCount(likeCount)
                .createAt(notice.getCreateAt())
                .createId(notice.getCreateId())
                .updateAt(notice.getUpdateAt())
                .updateId(notice.getUpdateId())
                .build();
    }
}
