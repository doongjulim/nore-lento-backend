package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.coupon.dto.FindCouponResponse;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindCouponsServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private FindCouponsService findCouponsService;

    @Test
    @DisplayName("findCoupons - 쿠폰 목록을 페이지로 반환한다")
    void findCoupons_shouldReturnCouponPage() {
        Coupon coupon1 = Coupon.builder().id(1L).name("1000원 할인").discountType(DiscountType.FIXED).discountValue(1000L).build();
        Coupon coupon2 = Coupon.builder().id(2L).name("10% 할인").discountType(DiscountType.PERCENTAGE).discountValue(10L).build();
        Pageable pageable = PageRequest.of(0, 10);

        given(couponRepository.findAll(pageable)).willReturn(new PageImpl<>(List.of(coupon1, coupon2)));

        Page<FindCouponResponse> result = findCouponsService.findCoupons(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo("1000원 할인");
        assertThat(result.getContent().get(1).getName()).isEqualTo("10% 할인");
    }

    @Test
    @DisplayName("findCoupons - 쿠폰이 없으면 빈 페이지를 반환한다")
    void findCoupons_shouldReturnEmptyPage_whenNoCoupons() {
        Pageable pageable = PageRequest.of(0, 10);

        given(couponRepository.findAll(pageable)).willReturn(Page.empty());

        Page<FindCouponResponse> result = findCouponsService.findCoupons(pageable);

        assertThat(result.getContent()).isEmpty();
    }
}
