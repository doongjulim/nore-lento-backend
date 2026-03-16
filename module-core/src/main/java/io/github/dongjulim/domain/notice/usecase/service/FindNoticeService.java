package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.notice.dto.FindNoticeResponse;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import io.github.dongjulim.domain.notice.usecase.FindNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindNoticeService implements FindNoticeUseCase {

    private final NoticeRepository noticeRepository;

    @Override
    public List<FindNoticeResponse> findNotice() {
        return noticeRepository.findAllByDeleteCheckFalse().stream()
                .map(FindNoticeResponse::from)
                .toList();
    }
}
