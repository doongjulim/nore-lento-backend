package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.AlreadyLikedException;
import io.github.dongjulim.domain.common.exception.NoticeNotFoundException;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.entity.NoticeLike;
import io.github.dongjulim.domain.notice.repository.NoticeLikeRepository;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import io.github.dongjulim.domain.notice.usecase.LikeNoticeUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeNoticeService implements LikeNoticeUseCase {

    private final NoticeRepository noticeRepository;
    private final UserLoader userLoader;
    private final NoticeLikeRepository noticeLikeRepository;

    @Override
    public void likeNotice(Long noticeId, String username) {
        Notice notice = noticeRepository.findByIdAndDeleteCheckFalse(noticeId)
                .orElseThrow(NoticeNotFoundException::new);
        User user = userLoader.load(username);

        if (noticeLikeRepository.existsByNoticeIdAndUserId(noticeId, user.getId())) {
            throw new AlreadyLikedException();
        }

        noticeLikeRepository.save(NoticeLike.builder()
                .notice(notice)
                .user(user)
                .build());
    }
}
