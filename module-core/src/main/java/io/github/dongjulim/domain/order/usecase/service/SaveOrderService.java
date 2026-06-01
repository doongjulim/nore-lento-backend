package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.common.exception.CouponAlreadyUsedException;
import io.github.dongjulim.domain.common.exception.CouponExpiredException;
import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.common.exception.OrderAmountNotEnoughException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.order.dto.OrderItemRequest;
import io.github.dongjulim.domain.order.dto.SaveOrderRequest;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.order.usecase.SaveOrderUseCase;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveOrderService implements SaveOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;
    private final UserLoader userLoader;

    @Override
    public void saveOrder(SaveOrderRequest request, String username) {
        User user = userLoader.load(username);

        Long resolvedShippingAddressId = resolveShippingAddressId(request.getShippingAddressId(), user.getId());

        long totalPrice = 0L;
        for (OrderItemRequest item : request.getOrderItems()) {
            Product product = productRepository.findByIdAndDeleteCheckFalse(item.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            totalPrice += product.getPrice() * item.getQuantity();
        }

        if (request.getUserCouponId() != null) {
            totalPrice = applyCoupon(request.getUserCouponId(), user.getId(), totalPrice);
        }

        Order order = orderRepository.save(Order.builder()
                .userId(user.getId())
                .shippingAddressId(resolvedShippingAddressId)
                .totalPrice(totalPrice)
                .build());

        for (OrderItemRequest item : request.getOrderItems()) {
            Product product = productRepository.findByIdAndDeleteCheckFalse(item.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            Stock stock = stockRepository.findByProductId(product.getId())
                    .orElseThrow(StockNotFoundException::new);
            stock.decrease(item.getQuantity());
            orderItemRepository.save(OrderItem.builder()
                    .orderId(order.getId())
                    .productId(product.getId())
                    .quantity(item.getQuantity())
                    .price(product.getPrice())
                    .build());
        }
    }

    private Long resolveShippingAddressId(Long requestedId, Long userId) {
        if (requestedId != null) {
            shippingAddressRepository.findByIdAndUserId(requestedId, userId)
                    .orElseThrow(ShippingAddressNotFoundException::new);
            return requestedId;
        }
        return shippingAddressRepository.findByUserIdAndIsDefaultTrue(userId)
                .map(ShippingAddress::getId)
                .orElseThrow(ShippingAddressNotFoundException::new);
    }

    private long applyCoupon(Long userCouponId, Long userId, long totalPrice) {
        UserCoupon userCoupon = userCouponRepository.findByIdAndUserId(userCouponId, userId)
                .orElseThrow(CouponNotFoundException::new);
        if (userCoupon.getIsUsed()) throw new CouponAlreadyUsedException();

        Coupon coupon = couponRepository.findById(userCoupon.getCouponId())
                .orElseThrow(CouponNotFoundException::new);
        if (coupon.isExpired()) throw new CouponExpiredException();
        if (!coupon.isApplicable(totalPrice)) throw new OrderAmountNotEnoughException();

        userCoupon.use();
        return coupon.applyDiscount(totalPrice);
    }
}
