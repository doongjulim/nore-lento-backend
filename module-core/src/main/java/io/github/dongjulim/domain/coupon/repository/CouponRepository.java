package io.github.dongjulim.domain.coupon.repository;

import io.github.dongjulim.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
