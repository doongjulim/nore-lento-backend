package io.github.dongjulim.domain.notice.repository;

import io.github.dongjulim.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByIdAndDeleteCheckFalse(Long id);

    List<Notice> findAllByDeleteCheckFalse();
}
