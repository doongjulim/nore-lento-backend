package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.coupon.usecase.NotifyCouponExpiryUseCase;
import io.github.dongjulim.domain.notification.service.AsyncNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotifyCouponExpiryService implements NotifyCouponExpiryUseCase {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final AsyncNotificationService asyncNotificationService;

    @Override
    public void notifyCouponExpiry() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysLater = now.plusDays(3);

        List<Coupon> expiringSoonCoupons = couponRepository.findAllByExpiresAtBetween(now, threeDaysLater);
        if (expiringSoonCoupons.isEmpty()) return;

        List<Long> couponIds = expiringSoonCoupons.stream()
                .map(Coupon::getId)
                .collect(Collectors.toList());

        Map<Long, Coupon> couponMap = expiringSoonCoupons.stream()
                .collect(Collectors.toMap(Coupon::getId, c -> c));

        List<UserCoupon> userCoupons = userCouponRepository.findAllByCouponIdInAndIsUsedFalse(couponIds);

        for (UserCoupon userCoupon : userCoupons) {
            Coupon coupon = couponMap.get(userCoupon.getCouponId());
            asyncNotificationService.send(
                    userCoupon.getUserId(),
                    "쿠폰 만료 임박 알림",
                    String.format("'%s' 쿠폰이 %s에 만료됩니다. 기간 내에 사용해 주세요.",
                            coupon.getName(), coupon.getExpiresAt().toLocalDate())
            );
        }
    }
}
