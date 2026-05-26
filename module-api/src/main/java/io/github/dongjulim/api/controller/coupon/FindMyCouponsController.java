package io.github.dongjulim.api.controller.coupon;

import io.github.dongjulim.domain.coupon.dto.FindMyCouponResponse;
import io.github.dongjulim.domain.coupon.usecase.FindMyCouponsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindMyCouponsController {

    private final FindMyCouponsUseCase findMyCouponsUseCase;

    @GetMapping("/api/v2/coupons/mine")
    public ResponseEntity<List<FindMyCouponResponse>> findMyCoupons(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(findMyCouponsUseCase.findMyCoupons(userDetails.getUsername()));
    }
}
