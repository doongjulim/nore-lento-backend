package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.NoticeNotFoundException;
import io.github.dongjulim.domain.notice.dto.UpdateNoticeRequest;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import io.github.dongjulim.domain.notice.usecase.UpdateNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateNoticeService implements UpdateNoticeUseCase {

    private final NoticeRepository noticeRepository;

    @Override
    public void updateNotice(Long id, UpdateNoticeRequest request) {
        Notice notice = noticeRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(NoticeNotFoundException::new);
        notice.updateNotice(request.getTitle(), request.getContent(), request.getCategory());
    }
}
