package io.github.dongjulim.domain.user.usecase;

public interface UpdatePasswordUseCase {
    void updatePassword(Long id, String currentPassword, String newPassword);
}
