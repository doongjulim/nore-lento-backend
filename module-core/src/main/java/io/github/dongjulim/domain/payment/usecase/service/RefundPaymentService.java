package io.github.dongjulim.domain.payment.usecase.service;

import io.github.dongjulim.domain.common.exception.OrderNotFoundException;
import io.github.dongjulim.domain.common.exception.PaymentNotFoundException;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.payment.entity.Payment;
import io.github.dongjulim.domain.payment.gateway.PaymentGateway;
import io.github.dongjulim.domain.payment.repository.PaymentRepository;
import io.github.dongjulim.domain.payment.usecase.RefundPaymentUseCase;
import io.github.dongjulim.domain.point.usecase.RefundPointUseCase;
import io.github.dongjulim.domain.point.usecase.RevokePointUseCase;
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
    private final UserCouponRepository userCouponRepository;
    private final UserLoader userLoader;
    private final RevokePointUseCase revokePointUseCase;
    private final RefundPointUseCase refundPointUseCase;
    private final PaymentGateway paymentGateway;

    @Override
    public void refundPayment(Long paymentId, String username) {
        User user = userLoader.load(username);

        Payment payment = paymentRepository.findByIdAndUserId(paymentId, user.getId())
                .orElseThrow(PaymentNotFoundException::new);

        payment.refund(); // COMPLETED가 아니면 PaymentNotRefundableException

        paymentGateway.cancel(payment.getTransactionId());

        Order order = orderRepository.findById(payment.getOrderId())
                .orElseThrow(OrderNotFoundException::new);

        order.cancelByRefund();

        List<OrderItem> items = orderItemRepository.findAllByOrderId(order.getId());
        for (OrderItem item : items) {
            stockRepository.findByProductId(item.getProductId())
                    .ifPresent(stock -> stock.increase(item.getQuantity()));
        }

        if (order.getUserCouponId() != null) {
            userCouponRepository.findById(order.getUserCouponId())
                    .ifPresent(uc -> uc.unuse());
        }

        long earnedPoints = order.getTotalPrice() / 100;
        if (earnedPoints > 0) {
            revokePointUseCase.revokePoint(user.getId(), earnedPoints);
        }

        if (order.getUsedPoints() != null && order.getUsedPoints() > 0) {
            refundPointUseCase.refundPoint(user.getId(), order.getUsedPoints());
        }
    }
}
