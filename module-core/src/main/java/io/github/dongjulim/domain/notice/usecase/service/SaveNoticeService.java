package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.notice.dto.SaveNoticeRequest;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import io.github.dongjulim.domain.notice.usecase.SaveNoticeUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveNoticeService implements SaveNoticeUseCase {

    private final NoticeRepository noticeRepository;
    private final UserLoader userLoader;

    @Override
    public void saveNotice(SaveNoticeRequest request, String username) {
        User user = userLoader.load(username);
        Notice notice = Notice.builder()
                .userId(user.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .build();
        noticeRepository.save(notice);
    }
}
