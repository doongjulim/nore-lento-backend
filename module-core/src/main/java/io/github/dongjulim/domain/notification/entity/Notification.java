package io.github.dongjulim.domain.notification.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "notification_seq", allocationSize = 1)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false, columnDefinition = "default 'false'")
    private Boolean isRead;

    @Column(columnDefinition = "default 'false'")
    private Boolean deleteCheck;

    @Builder
    public Notification(
            Long id,
            Long userId,
            String title,
            String content,
            Boolean isRead,
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
        this.isRead = isRead != null ? isRead : false;
        this.deleteCheck = deleteCheck != null ? deleteCheck : false;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public void deleteNotification() {
        this.deleteCheck = true;
    }
}
