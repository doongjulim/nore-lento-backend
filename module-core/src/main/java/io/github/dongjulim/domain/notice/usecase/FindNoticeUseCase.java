package io.github.dongjulim.domain.notice.usecase;

import io.github.dongjulim.domain.notice.dto.FindNoticeRequest;
import io.github.dongjulim.domain.notice.dto.FindNoticeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindNoticeUseCase {

    Page<FindNoticeResponse> findNotice(FindNoticeRequest request, Pageable pageable);
}
