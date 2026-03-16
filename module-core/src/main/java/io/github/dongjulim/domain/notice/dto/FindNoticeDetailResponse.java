package io.github.dongjulim.domain.notice.dto;

import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.enums.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindNoticeDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Category category;
    private final Long likeCount;
    private final LocalDateTime createAt;
    private final String createBy;
    private final LocalDateTime updateAt;
    private final String updateBy;

    public static FindNoticeDetailResponse from(Notice notice, Long likeCount) {
        return FindNoticeDetailResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .category(notice.getCategory())
                .likeCount(likeCount)
                .createAt(notice.getCreateAt())
                .createBy(notice.getCreateBy())
                .updateAt(notice.getUpdateAt())
                .updateBy(notice.getUpdateBy())
                .build();
    }
}
