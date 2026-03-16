package io.github.dongjulim.domain.common.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createAt;

    @CreatedBy
    @Column(updatable = false)
    private String createId;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @LastModifiedBy
    private String updateId;

    protected BaseEntity(LocalDateTime createAt, String createId, LocalDateTime updateAt, String updateId) {
        this.createAt = createAt;
        this.createId = createId;
        this.updateAt = updateAt;
        this.updateId = updateId;
    }
}
