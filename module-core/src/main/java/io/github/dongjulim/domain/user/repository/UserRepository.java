package io.github.dongjulim.domain.user.repository;

import io.github.dongjulim.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndDeleteCheck(String username, Boolean deleteCheck);

    Optional<User> findByIdAndDeleteCheckFalse(Long id);

    List<User> findAllByDeleteCheckFalse();
}
