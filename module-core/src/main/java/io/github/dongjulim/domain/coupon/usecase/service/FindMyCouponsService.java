package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.coupon.dto.FindMyCouponResponse;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.coupon.usecase.FindMyCouponsUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindMyCouponsService implements FindMyCouponsUseCase {

    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;
    private final UserLoader userLoader;

    @Override
    public List<FindMyCouponResponse> findMyCoupons(String username) {
        User user = userLoader.load(username);

        return userCouponRepository.findAllByUserId(user.getId()).stream()
                .flatMap(userCoupon -> couponRepository.findById(userCoupon.getCouponId())
                        .map(coupon -> FindMyCouponResponse.from(userCoupon, coupon))
                        .stream())
                .collect(Collectors.toList());
    }
}
