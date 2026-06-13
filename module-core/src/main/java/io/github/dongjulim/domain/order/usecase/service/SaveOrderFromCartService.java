package io.github.dongjulim.domain.order.usecase.service;

import io.github.dongjulim.domain.cart.entity.Cart;
import io.github.dongjulim.domain.cart.entity.CartItem;
import io.github.dongjulim.domain.cart.repository.CartRepository;
import io.github.dongjulim.domain.common.exception.CartEmptyException;
import io.github.dongjulim.domain.common.exception.CartNotFoundException;
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
import io.github.dongjulim.domain.order.dto.SaveOrderFromCartRequest;
import io.github.dongjulim.domain.order.entity.Order;
import io.github.dongjulim.domain.order.entity.OrderItem;
import io.github.dongjulim.domain.order.repository.OrderItemRepository;
import io.github.dongjulim.domain.order.repository.OrderRepository;
import io.github.dongjulim.domain.order.usecase.SaveOrderFromCartUseCase;
import io.github.dongjulim.domain.point.entity.PointHistory;
import io.github.dongjulim.domain.point.entity.UserPoint;
import io.github.dongjulim.domain.point.enums.PointHistoryType;
import io.github.dongjulim.domain.point.repository.PointHistoryRepository;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveOrderFromCartService implements SaveOrderFromCartUseCase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;
    private final UserLoader userLoader;
    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public void saveOrderFromCart(SaveOrderFromCartRequest request, String username) {
        User user = userLoader.load(username);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new CartEmptyException();
        }

        Long resolvedShippingAddressId = resolveShippingAddressId(request.getShippingAddressId(), user.getId());

        long totalPrice = 0L;
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findByIdAndDeleteCheckFalse(cartItem.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            totalPrice += product.getPrice() * cartItem.getQuantity();
        }

        if (request.getUserCouponId() != null) {
            totalPrice = applyCoupon(request.getUserCouponId(), user.getId(), totalPrice);
        }

        if (request.getUsePoints() != null && request.getUsePoints() > 0) {
            totalPrice = applyPoints(request.getUsePoints(), user.getId(), totalPrice);
        }

        Order order = orderRepository.save(Order.builder()
                .userId(user.getId())
                .shippingAddressId(resolvedShippingAddressId)
                .totalPrice(totalPrice)
                .build());

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findByIdAndDeleteCheckFalse(cartItem.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            Stock stock = stockRepository.findByProductId(product.getId())
                    .orElseThrow(StockNotFoundException::new);
            stock.decrease(cartItem.getQuantity());
            orderItemRepository.save(OrderItem.builder()
                    .orderId(order.getId())
                    .productId(product.getId())
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build());
        }

        cart.clearItems();
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

    private long applyPoints(Long usePoints, Long userId, long totalPrice) {
        UserPoint userPoint = userPointRepository.findByUserId(userId)
                .orElseThrow(() -> new io.github.dongjulim.domain.common.exception.InsufficientPointException());
        userPoint.use(usePoints);
        pointHistoryRepository.save(PointHistory.builder()
                .userId(userId)
                .amount(usePoints)
                .type(PointHistoryType.USE)
                .build());
        return totalPrice - usePoints;
    }
}
