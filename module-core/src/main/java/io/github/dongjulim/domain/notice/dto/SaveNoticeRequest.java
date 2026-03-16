package io.github.dongjulim.domain.notice.dto;

import io.github.dongjulim.domain.notice.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SaveNoticeRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Category category;
}
