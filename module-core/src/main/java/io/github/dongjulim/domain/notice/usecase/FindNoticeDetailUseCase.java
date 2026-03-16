package io.github.dongjulim.domain.notice.usecase;

import io.github.dongjulim.domain.notice.dto.FindNoticeDetailResponse;

public interface FindNoticeDetailUseCase {

    FindNoticeDetailResponse findNoticeDetail(Long id);
}
