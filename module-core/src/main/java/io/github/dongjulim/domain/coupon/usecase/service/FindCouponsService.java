package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.coupon.dto.FindCouponResponse;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.usecase.FindCouponsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCouponsService implements FindCouponsUseCase {

    private final CouponRepository couponRepository;

    @Override
    public Page<FindCouponResponse> findCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable).map(FindCouponResponse::from);
    }
}
