package io.github.dongjulim.domain.notice.dto;

import io.github.dongjulim.domain.notice.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class FindNoticeRequest {

    @Enumerated(EnumType.STRING)
    private Category category;
    private String keyword;
}
