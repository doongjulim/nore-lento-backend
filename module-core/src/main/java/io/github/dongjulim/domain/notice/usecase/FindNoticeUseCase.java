package io.github.dongjulim.domain.notice.usecase;

import io.github.dongjulim.domain.notice.dto.FindNoticeResponse;

import java.util.List;

public interface FindNoticeUseCase {

    List<FindNoticeResponse> findNotice();
}
