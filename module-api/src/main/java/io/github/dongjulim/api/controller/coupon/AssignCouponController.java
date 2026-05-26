package io.github.dongjulim.api.controller.coupon;

import io.github.dongjulim.domain.coupon.usecase.AssignCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AssignCouponController {

    private final AssignCouponUseCase assignCouponUseCase;

    @PostMapping("/api/v2/coupons/{couponId}/users/{userId}")
    public ResponseEntity<Void> assignCoupon(
            @PathVariable Long couponId,
            @PathVariable Long userId
    ) {
        assignCouponUseCase.assignCoupon(couponId, userId);
        return ResponseEntity.ok().build();
    }
}
