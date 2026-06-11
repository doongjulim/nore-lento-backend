package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.coupon.dto.UpdateCouponRequest;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateCouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private UpdateCouponService updateCouponService;

    @Test
    @DisplayName("updateCoupon - 요청한 필드가 정상적으로 수정된다")
    void updateCoupon_shouldUpdateFields() {
        Coupon coupon = Coupon.builder()
                .id(1L).name("기존 쿠폰").discountType(DiscountType.FIXED)
                .discountValue(1000L).minOrderAmount(5000L)
                .expiresAt(LocalDateTime.of(2026, 12, 31, 0, 0)).build();

        UpdateCouponRequest request = new UpdateCouponRequest();
        ReflectionTestUtils.setField(request, "name", "수정된 쿠폰");
        ReflectionTestUtils.setField(request, "discountValue", 2000L);
        ReflectionTestUtils.setField(request, "minOrderAmount", 10000L);
        LocalDateTime newExpiry = LocalDateTime.of(2027, 6, 30, 0, 0);
        ReflectionTestUtils.setField(request, "expiresAt", newExpiry);

        given(couponRepository.findById(1L)).willReturn(Optional.of(coupon));

        updateCouponService.updateCoupon(1L, request);

        assertThat(coupon.getName()).isEqualTo("수정된 쿠폰");
        assertThat(coupon.getDiscountValue()).isEqualTo(2000L);
        assertThat(coupon.getMinOrderAmount()).isEqualTo(10000L);
        assertThat(coupon.getExpiresAt()).isEqualTo(newExpiry);
    }

    @Test
    @DisplayName("updateCoupon - null 필드는 기존 값이 유지된다")
    void updateCoupon_shouldKeepExistingValues_whenFieldIsNull() {
        Coupon coupon = Coupon.builder()
                .id(1L).name("기존 쿠폰").discountType(DiscountType.FIXED)
                .discountValue(1000L).minOrderAmount(5000L).build();

        UpdateCouponRequest request = new UpdateCouponRequest();
        ReflectionTestUtils.setField(request, "name", "수정된 쿠폰");
        // discountValue, minOrderAmount, expiresAt는 null

        given(couponRepository.findById(1L)).willReturn(Optional.of(coupon));

        updateCouponService.updateCoupon(1L, request);

        assertThat(coupon.getName()).isEqualTo("수정된 쿠폰");
        assertThat(coupon.getDiscountValue()).isEqualTo(1000L); // 그대로
        assertThat(coupon.getMinOrderAmount()).isEqualTo(5000L); // 그대로
    }

    @Test
    @DisplayName("updateCoupon - 존재하지 않는 쿠폰이면 CouponNotFoundException을 던진다")
    void updateCoupon_throwsCouponNotFoundException_whenCouponNotFound() {
        UpdateCouponRequest request = new UpdateCouponRequest();
        ReflectionTestUtils.setField(request, "name", "수정된 쿠폰");

        given(couponRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateCouponService.updateCoupon(99L, request))
                .isInstanceOf(CouponNotFoundException.class);
    }
}
