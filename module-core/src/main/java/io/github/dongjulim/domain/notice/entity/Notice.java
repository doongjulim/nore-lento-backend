package io.github.dongjulim.domain.notice.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "notice_seq", allocationSize = 1)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_seq")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column
    @ColumnDefault("false")
    private Boolean deleteCheck;

    @Builder
    public Notice(
            Long id,
            String title,
            String content,
            Boolean deleteCheck,
            LocalDateTime createAt,
            String createId,
            LocalDateTime updateAt,
            String updateId
    ) {
        super(createAt, createId, updateAt, updateId);
        this.id = id;
        this.title = title;
        this.content = content;
        this.deleteCheck = deleteCheck;
    }

    public void updateNotice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void deleteNotice() {
        this.deleteCheck = true;
    }
}
