package io.github.dongjulim.domain.payment.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.common.exception.PaymentNotFoundException;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.payment.entity.Payment;
import io.github.dongjulim.domain.payment.repository.PaymentRepository;
import io.github.dongjulim.domain.payment.usecase.RefundPaymentUseCase;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundPaymentService implements RefundPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StockRepository stockRepository;
    private final UserLoader userLoader;

    @Override
    public void refundPayment(Long paymentId, String username) {
        User user = userLoader.load(username);

        Payment payment = paymentRepository.findByIdAndUserId(paymentId, user.getId())
                .orElseThrow(PaymentNotFoundException::new);

        payment.refund(); // COMPLETED가 아니면 PaymentNotRefundableException

        Order order = orderRepository.findById(payment.getOrderId())
                .orElseThrow(OrderNotFoundException::new);

        order.cancelByRefund();

        List<OrderItem> items = orderItemRepository.findAllByOrderId(order.getId());
        for (OrderItem item : items) {
            stockRepository.findByProductId(item.getProductId())
                    .ifPresent(stock -> stock.increase(item.getQuantity()));
        }
    }
}
