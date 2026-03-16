package io.github.dongjulim.domain.notice.usecase;

import io.github.dongjulim.domain.notice.dto.UpdateNoticeRequest;

public interface UpdateNoticeUseCase {

    void updateNotice(Long id, UpdateNoticeRequest request);
}
