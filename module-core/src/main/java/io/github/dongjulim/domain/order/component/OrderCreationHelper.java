package io.github.dongjulim.domain.order.component;

import io.github.dongjulim.domain.common.exception.CouponAlreadyUsedException;
import io.github.dongjulim.domain.common.exception.CouponExpiredException;
import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.common.exception.OrderAmountNotEnoughException;
import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.point.usecase.UsePointUseCase;
import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreationHelper {

    private final ShippingAddressRepository shippingAddressRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;
    private final UsePointUseCase usePointUseCase;

    public Long resolveShippingAddressId(Long requestedId, Long userId) {
        if (requestedId != null) {
            shippingAddressRepository.findByIdAndUserId(requestedId, userId)
                    .orElseThrow(ShippingAddressNotFoundException::new);
            return requestedId;
        }
        return shippingAddressRepository.findByUserIdAndIsDefaultTrue(userId)
                .map(ShippingAddress::getId)
                .orElseThrow(ShippingAddressNotFoundException::new);
    }

    public long applyCoupon(Long userCouponId, Long userId, long totalPrice) {
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

    public long applyPoints(Long usePoints, Long userId, long totalPrice) {
        usePointUseCase.usePoint(userId, usePoints);
        return totalPrice - usePoints;
    }
}
