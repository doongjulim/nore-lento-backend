package io.github.dongjulim.domain.notice.usecase;

public interface LikeNoticeUseCase {

    void likeNotice(Long noticeId, String username);
}
