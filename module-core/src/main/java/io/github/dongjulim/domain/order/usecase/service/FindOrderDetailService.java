package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.order.dto.FindOrderDetailResponse;
import io.github.dongjulim.domain.order.dto.OrderItemResponse;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.order.usecase.FindOrderDetailUseCase;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
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
public class FindOrderDetailService implements FindOrderDetailUseCase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final UserLoader userLoader;

    @Override
    public FindOrderDetailResponse findOrderDetail(Long orderId, String username) {
        User user = userLoader.load(username);

        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(OrderNotFoundException::new);

        List<OrderItem> items = orderItemRepository.findAllByOrderId(order.getId());

        boolean isCompleted = order.getStatus() == OrderStatus.COMPLETED;

        List<OrderItemResponse> itemResponses = items.stream()
                .map(item -> {
                    boolean reviewable = isCompleted &&
                            !reviewRepository.existsByUserIdAndProductIdAndDeleteCheckFalse(
                                    user.getId(), item.getProductId());
                    return OrderItemResponse.from(item, reviewable);
                })
                .collect(Collectors.toList());

        return FindOrderDetailResponse.from(order, itemResponses);
    }
}
