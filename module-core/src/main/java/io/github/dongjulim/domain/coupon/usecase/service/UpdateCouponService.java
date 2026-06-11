package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.coupon.dto.UpdateCouponRequest;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.usecase.UpdateCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateCouponService implements UpdateCouponUseCase {

    private final CouponRepository couponRepository;

    @Override
    public void updateCoupon(Long couponId, UpdateCouponRequest request) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(CouponNotFoundException::new);
        coupon.update(request.getName(), request.getDiscountValue(),
                request.getMinOrderAmount(), request.getExpiresAt());
    }
}
