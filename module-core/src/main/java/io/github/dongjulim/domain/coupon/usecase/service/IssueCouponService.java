package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.coupon.dto.IssueCouponRequest;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.usecase.IssueCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class IssueCouponService implements IssueCouponUseCase {

    private final CouponRepository couponRepository;

    @Override
    public void issueCoupon(IssueCouponRequest request) {
        couponRepository.save(Coupon.builder()
                .name(request.getName())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .minOrderAmount(request.getMinOrderAmount())
                .expiresAt(request.getExpiresAt())
                .build());
    }
}
