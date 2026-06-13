package io.github.dongjulim.api.controller.coupon;

import io.github.dongjulim.domain.coupon.usecase.DeleteCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteCouponController {

    private final DeleteCouponUseCase deleteCouponUseCase;

    @DeleteMapping("/api/v2/coupons/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        deleteCouponUseCase.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }
}
