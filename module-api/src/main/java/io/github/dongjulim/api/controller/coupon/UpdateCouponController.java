package io.github.dongjulim.api.controller.coupon;

import io.github.dongjulim.domain.coupon.dto.UpdateCouponRequest;
import io.github.dongjulim.domain.coupon.usecase.UpdateCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateCouponController {

    private final UpdateCouponUseCase updateCouponUseCase;

    @PatchMapping("/api/v2/coupons/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Void> updateCoupon(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCouponRequest request
    ) {
        updateCouponUseCase.updateCoupon(id, request);
        return ResponseEntity.ok().build();
    }
}
