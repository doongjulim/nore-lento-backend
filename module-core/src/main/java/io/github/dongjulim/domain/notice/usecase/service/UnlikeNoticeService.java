package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.NoticeLikeNotFoundException;
import io.github.dongjulim.domain.notice.entity.NoticeLike;
import io.github.dongjulim.domain.notice.repository.NoticeLikeRepository;
import io.github.dongjulim.domain.notice.usecase.UnlikeNoticeUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UnlikeNoticeService implements UnlikeNoticeUseCase {

    private final UserLoader userLoader;
    private final NoticeLikeRepository noticeLikeRepository;

    @Override
    public void unlikeNotice(Long noticeId, String username) {
        User user = userLoader.load(username);

        NoticeLike noticeLike = noticeLikeRepository.findByNoticeIdAndUserId(noticeId, user.getId())
                .orElseThrow(NoticeLikeNotFoundException::new);

        noticeLikeRepository.delete(noticeLike);
    }
}
