package io.github.dongjulim.domain.notice.repository;

import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByIdAndDeleteCheckFalse(Long id);

    @Query("SELECT n FROM notice n WHERE n.deleteCheck = false" +
            " AND (:category IS NULL OR n.category = :category)" +
            " AND (:keyword IS NULL OR n.title LIKE %:keyword%)")
    Page<Notice> findAllBySearchCondition(
            @Param("category") Category category,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    List<Notice> findAllByDeleteCheckFalseAndCategoryAndContentLike(Category category, String keyword,Pageable pageable);
}
