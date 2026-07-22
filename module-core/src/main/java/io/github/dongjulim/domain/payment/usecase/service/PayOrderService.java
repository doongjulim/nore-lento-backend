package io.github.dongjulim.domain.payment.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.payment.dto.PayOrderRequest;
import io.github.dongjulim.domain.payment.entity.Payment;
import io.github.dongjulim.domain.payment.enums.PaymentStatus;
import io.github.dongjulim.domain.payment.gateway.PaymentGateway;
import io.github.dongjulim.domain.payment.repository.PaymentRepository;
import io.github.dongjulim.domain.payment.usecase.PayOrderUseCase;
import io.github.dongjulim.domain.point.usecase.EarnPointUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PayOrderService implements PayOrderUseCase {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserLoader userLoader;
    private final EarnPointUseCase earnPointUseCase;
    private final PaymentGateway paymentGateway;

    @Override
    public void payOrder(PayOrderRequest request, String username) {
        User user = userLoader.load(username);

        Order order = orderRepository.findByIdAndUserId(request.getOrderId(), user.getId())
                .orElseThrow(OrderNotFoundException::new);

        order.complete(); // PENDING이 아니면 OrderNotPayableException

        String transactionId = paymentGateway.approve(order.getId(), order.getTotalPrice(), request.getMethod());

        paymentRepository.save(Payment.builder()
                .orderId(order.getId())
                .userId(user.getId())
                .method(request.getMethod())
                .status(PaymentStatus.COMPLETED)
                .amount(order.getTotalPrice())
                .transactionId(transactionId)
                .build());

        earnPointUseCase.earnPoint(user.getId(), order.getTotalPrice());
    }
}
