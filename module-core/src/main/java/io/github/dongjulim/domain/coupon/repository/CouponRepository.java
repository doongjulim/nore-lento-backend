package io.github.dongjulim.domain.coupon.repository;

import io.github.dongjulim.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM coupon c WHERE c.expiresAt BETWEEN :from AND :to")
    List<Coupon> findAllByExpiresAtBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
