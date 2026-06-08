package io.github.dongjulim.domain.point.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.common.exception.InsufficientPointException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "user_point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "user_point_seq", allocationSize = 1)
public class UserPoint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_point_seq")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Long balance;

    @Builder
    public UserPoint(Long id, Long userId, Long balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance != null ? balance : 0L;
    }

    public void earn(long amount) {
        this.balance += amount;
    }

    public void use(long amount) {
        if (this.balance < amount) {
            throw new InsufficientPointException();
        }
        this.balance -= amount;
    }
}
