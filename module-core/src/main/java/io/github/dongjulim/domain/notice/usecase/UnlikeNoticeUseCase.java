package io.github.dongjulim.domain.notice.usecase;

public interface UnlikeNoticeUseCase {

    void unlikeNotice(Long noticeId, String username);
}
