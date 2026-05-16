package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.order.dto.FindOrderDetailResponse;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.order.usecase.FindOrderDetailUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindOrderDetailService implements FindOrderDetailUseCase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserLoader userLoader;

    @Override
    public FindOrderDetailResponse findOrderDetail(Long orderId, String username) {
        User user = userLoader.load(username);

        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(OrderNotFoundException::new);

        List<OrderItem> items = orderItemRepository.findAllByOrderId(order.getId());

        return FindOrderDetailResponse.from(order, items);
    }
}
