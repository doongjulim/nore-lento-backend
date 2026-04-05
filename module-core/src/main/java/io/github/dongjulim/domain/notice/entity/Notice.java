package io.github.dongjulim.domain.notice.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.notice.enums.Category;
import io.github.dongjulim.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name= "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "default 'false'")
    private Boolean deleteCheck;

    @Builder
    public Notice(
            Long id,
            Long userId,
            String title,
            String content,
            Category category,
            Boolean deleteCheck,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.deleteCheck = deleteCheck;
    }

    public void updateNotice(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void deleteNotice() {
        this.deleteCheck = true;
    }
}
