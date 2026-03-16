package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.NoticeNotFoundException;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import io.github.dongjulim.domain.notice.usecase.DeleteNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteNoticeService implements DeleteNoticeUseCase {

    private final NoticeRepository noticeRepository;

    @Override
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(NoticeNotFoundException::new);
        notice.deleteNotice();
    }
}
