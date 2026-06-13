package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DeleteCouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private DeleteCouponService deleteCouponService;

    @Test
    @DisplayName("deleteCoupon - 쿠폰이 정상적으로 삭제된다")
    void deleteCoupon_shouldDeleteCoupon() {
        Coupon coupon = Coupon.builder().id(1L).name("테스트 쿠폰")
                .discountType(DiscountType.FIXED).discountValue(1000L).build();
        given(couponRepository.findById(1L)).willReturn(Optional.of(coupon));

        deleteCouponService.deleteCoupon(1L);

        then(couponRepository).should().delete(coupon);
    }

    @Test
    @DisplayName("deleteCoupon - 존재하지 않는 쿠폰이면 CouponNotFoundException을 던진다")
    void deleteCoupon_throwsCouponNotFoundException_whenCouponNotFound() {
        given(couponRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deleteCouponService.deleteCoupon(99L))
                .isInstanceOf(CouponNotFoundException.class);
    }
}
