package io.github.dongjulim.domain.notice.dto;

import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.enums.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindNoticeResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Category category;

    public static FindNoticeResponse from(Notice notice) {
        return FindNoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .category(notice.getCategory())
                .build();
    }
}
