package io.github.dongjulim.domain.coupon.repository;

import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    Optional<UserCoupon> findByIdAndUserId(Long id, Long userId);

    List<UserCoupon> findAllByUserId(Long userId);

    List<UserCoupon> findAllByCouponIdInAndIsUsedFalse(List<Long> couponIds);
}
