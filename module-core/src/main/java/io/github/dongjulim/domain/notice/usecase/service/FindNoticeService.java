package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.notice.dto.FindNoticeRequest;
import io.github.dongjulim.domain.notice.dto.FindNoticeResponse;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import io.github.dongjulim.domain.notice.usecase.FindNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindNoticeService implements FindNoticeUseCase {

    private final NoticeRepository noticeRepository;

    @Override
    public Page<FindNoticeResponse> findNotice(FindNoticeRequest request, Pageable pageable) {
        var list = noticeRepository.findAllBySearchCondition(request.getCategory(), request.getKeyword(), pageable)
                .map(FindNoticeResponse::from);
        System.out.println("#### : " + request.getKeyword());
        list.forEach( a -> System.out.println(a.getTitle()));
        return noticeRepository.findAllBySearchCondition(request.getCategory(), request.getKeyword(), pageable)
                .map(FindNoticeResponse::from);
    }
}
