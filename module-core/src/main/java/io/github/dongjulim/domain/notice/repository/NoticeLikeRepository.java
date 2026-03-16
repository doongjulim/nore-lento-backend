package io.github.dongjulim.domain.notice.repository;

import io.github.dongjulim.domain.notice.entity.NoticeLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeLikeRepository extends JpaRepository<NoticeLike, Long> {

    boolean existsByNoticeIdAndUserId(Long noticeId, Long userId);

    Optional<NoticeLike> findByNoticeIdAndUserId(Long noticeId, Long userId);

    Long countByNoticeId(Long noticeId);
}
