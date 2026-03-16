package io.github.dongjulim.domain.notice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SaveNoticeRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
