package io.github.dongjulim.domain.user.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.user.enums.Grade;
import io.github.dongjulim.domain.user.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "user_seq", allocationSize = 1)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(20)")
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column
    @ColumnDefault("false")
    private Boolean deleteCheck;

    @Builder
    public User(
            Long id,
            String username,
            String password,
            String name,
            Role role,
            Grade grade,
            Boolean deleteCheck,
            LocalDateTime createAt,
            String createId,
            LocalDateTime updateAt,
            String updateId
    ) {
        super(createAt, createId, updateAt, updateId);
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.grade = grade;
        this.deleteCheck = deleteCheck;
    }

    public void updateUser(String username, String name, Role role, Grade grade) {
        this.username = username;
        this.name = name;
        this.role = role;
        this.grade = grade;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void deleteUser() {
        this.deleteCheck = true;
    }
}
