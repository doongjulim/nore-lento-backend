package io.github.dongjulim.api.controller.coupon;

import io.github.dongjulim.domain.coupon.dto.IssueCouponRequest;
import io.github.dongjulim.domain.coupon.usecase.IssueCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class IssueCouponController {

    private final IssueCouponUseCase issueCouponUseCase;

    @PostMapping("/api/v2/coupons")
    public ResponseEntity<Void> issueCoupon(
            @RequestBody @Valid IssueCouponRequest request
    ) {
        issueCouponUseCase.issueCoupon(request);
        return ResponseEntity.ok().build();
    }
}
