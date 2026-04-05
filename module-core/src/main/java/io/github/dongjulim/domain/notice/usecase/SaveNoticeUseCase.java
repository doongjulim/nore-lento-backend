package io.github.dongjulim.domain.notice.usecase;

import io.github.dongjulim.domain.notice.dto.SaveNoticeRequest;

public interface SaveNoticeUseCase {

    void saveNotice(SaveNoticeRequest request, String username);
}
