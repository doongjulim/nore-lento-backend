package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.coupon.dto.IssueCouponRequest;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class IssueCouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private IssueCouponService issueCouponService;

    @Test
    @DisplayName("issueCoupon - 쿠폰이 정상적으로 저장된다")
    void issueCoupon_shouldSaveCoupon() {
        IssueCouponRequest request = new IssueCouponRequest();
        ReflectionTestUtils.setField(request, "name", "10% 할인 쿠폰");
        ReflectionTestUtils.setField(request, "discountType", DiscountType.PERCENTAGE);
        ReflectionTestUtils.setField(request, "discountValue", 10L);
        ReflectionTestUtils.setField(request, "minOrderAmount", 5000L);

        issueCouponService.issueCoupon(request);

        ArgumentCaptor<Coupon> captor = ArgumentCaptor.forClass(Coupon.class);
        then(couponRepository).should().save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("10% 할인 쿠폰");
        assertThat(captor.getValue().getDiscountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(captor.getValue().getDiscountValue()).isEqualTo(10L);
        assertThat(captor.getValue().getMinOrderAmount()).isEqualTo(5000L);
    }
}
