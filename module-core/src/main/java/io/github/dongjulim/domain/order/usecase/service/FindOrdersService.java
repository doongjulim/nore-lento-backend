package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.order.dto.FindOrderResponse;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.order.usecase.FindOrdersUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindOrdersService implements FindOrdersUseCase {

    private final OrderRepository orderRepository;
    private final UserLoader userLoader;

    @Override
    public List<FindOrderResponse> findOrders(String username) {
        User user = userLoader.load(username);

        return orderRepository.findAllByUserId(user.getId()).stream()
                .map(FindOrderResponse::from)
                .collect(Collectors.toList());
    }
}
