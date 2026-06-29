package io.github.dongjulim.domain.point.usecase;

public interface RevokePointUseCase {
    void revokePoint(Long userId, Long amount);
}
