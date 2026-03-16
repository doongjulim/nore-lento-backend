package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.NoticeLikeNotFoundException;
import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.notice.entity.NoticeLike;
import io.github.dongjulim.domain.notice.repository.NoticeLikeRepository;
import io.github.dongjulim.domain.notice.usecase.UnlikeNoticeUseCase;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UnlikeNoticeService implements UnlikeNoticeUseCase {

    private final UserRepository userRepository;
    private final NoticeLikeRepository noticeLikeRepository;

    @Override
    public void unlikeNotice(Long noticeId, String username) {
        User user = userRepository.findByUsernameAndDeleteCheck(username, false)
                .orElseThrow(UserNotFoundException::new);

        NoticeLike noticeLike = noticeLikeRepository.findByNoticeIdAndUserId(noticeId, user.getId())
                .orElseThrow(NoticeLikeNotFoundException::new);

        noticeLikeRepository.delete(noticeLike);
    }
}
