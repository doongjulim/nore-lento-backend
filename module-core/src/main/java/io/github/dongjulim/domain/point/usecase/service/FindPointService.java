package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.point.dto.FindPointResponse;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
import io.github.dongjulim.domain.point.usecase.FindPointUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindPointService implements FindPointUseCase {

    private final UserPointRepository userPointRepository;
    private final UserLoader userLoader;

    @Override
    public FindPointResponse findPoint(String username) {
        User user = userLoader.load(username);
        return userPointRepository.findByUserId(user.getId())
                .map(FindPointResponse::from)
                .orElse(FindPointResponse.empty());
    }
}
