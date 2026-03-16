package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.AlreadyLikedException;
import io.github.dongjulim.domain.common.exception.NoticeNotFoundException;
import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.entity.NoticeLike;
import io.github.dongjulim.domain.notice.repository.NoticeLikeRepository;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import io.github.dongjulim.domain.notice.usecase.LikeNoticeUseCase;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeNoticeService implements LikeNoticeUseCase {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final NoticeLikeRepository noticeLikeRepository;

    @Override
    public void likeNotice(Long noticeId, String username) {
        Notice notice = noticeRepository.findByIdAndDeleteCheckFalse(noticeId)
                .orElseThrow(NoticeNotFoundException::new);
        User user = userRepository.findByUsernameAndDeleteCheck(username, false)
                .orElseThrow(UserNotFoundException::new);

        if (noticeLikeRepository.existsByNoticeIdAndUserId(noticeId, user.getId())) {
            throw new AlreadyLikedException();
        }

        noticeLikeRepository.save(NoticeLike.builder()
                .notice(notice)
                .user(user)
                .build());
    }
}
