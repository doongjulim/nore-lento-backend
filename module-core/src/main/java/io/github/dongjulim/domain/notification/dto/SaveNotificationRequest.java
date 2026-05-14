package io.github.dongjulim.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SaveNotificationRequest {

    @NotNull(message = "대상 유저 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "알림 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "알림 내용은 필수입니다.")
    private String content;
}
