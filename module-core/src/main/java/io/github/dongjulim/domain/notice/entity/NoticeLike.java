package io.github.dongjulim.domain.notice.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "notice_like")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"notice_id", "user_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "notice_like_seq", allocationSize = 1)
public class NoticeLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_like_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public NoticeLike(
            Long id,
            Notice notice,
            User user,
            LocalDateTime createAt,
            String createId,
            LocalDateTime updateAt,
            String updateId
    ) {
        super(createAt, createId, updateAt, updateId);
        this.id = id;
        this.notice = notice;
        this.user = user;
    }
}
