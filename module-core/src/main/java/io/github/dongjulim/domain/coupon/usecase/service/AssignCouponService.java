package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.coupon.usecase.AssignCouponUseCase;
import io.github.dongjulim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignCouponService implements AssignCouponUseCase {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    @Override
    public void assignCoupon(Long couponId, Long userId) {
        couponRepository.findById(couponId)
                .orElseThrow(CouponNotFoundException::new);

        userRepository.findByIdAndDeleteCheckFalse(userId)
                .orElseThrow(UserNotFoundException::new);

        userCouponRepository.save(UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .build());
    }
}
