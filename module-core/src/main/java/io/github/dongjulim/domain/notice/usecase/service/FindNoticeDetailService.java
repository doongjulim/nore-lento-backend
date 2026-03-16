package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.NoticeNotFoundException;
import io.github.dongjulim.domain.notice.dto.FindNoticeDetailResponse;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.repository.NoticeLikeRepository;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import io.github.dongjulim.domain.notice.usecase.FindNoticeDetailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindNoticeDetailService implements FindNoticeDetailUseCase {

    private final NoticeRepository noticeRepository;
    private final NoticeLikeRepository noticeLikeRepository;

    @Override
    public FindNoticeDetailResponse findNoticeDetail(Long id) {
        Notice notice = noticeRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(NoticeNotFoundException::new);
        long likeCount = noticeLikeRepository.countByNoticeId(id);
        return FindNoticeDetailResponse.from(notice, likeCount);
    }
}
