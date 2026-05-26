package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.coupon.dto.FindMyCouponResponse;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindMyCouponsServiceTest {

    @Mock
    private UserCouponRepository userCouponRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindMyCouponsService findMyCouponsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findMyCoupons - 내 쿠폰 목록을 반환한다")
    void findMyCoupons_shouldReturnMyCoupons() {
        UserCoupon userCoupon1 = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        UserCoupon userCoupon2 = UserCoupon.builder().id(2L).userId(1L).couponId(20L).isUsed(true).build();
        Coupon coupon1 = Coupon.builder().id(10L).name("1000원 할인").discountType(DiscountType.FIXED).discountValue(1000L).build();
        Coupon coupon2 = Coupon.builder().id(20L).name("10% 할인").discountType(DiscountType.PERCENTAGE).discountValue(10L).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(userCouponRepository.findAllByUserId(1L)).willReturn(List.of(userCoupon1, userCoupon2));
        given(couponRepository.findById(10L)).willReturn(Optional.of(coupon1));
        given(couponRepository.findById(20L)).willReturn(Optional.of(coupon2));

        List<FindMyCouponResponse> result = findMyCouponsService.findMyCoupons("testuser");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("1000원 할인");
        assertThat(result.get(0).getIsUsed()).isFalse();
        assertThat(result.get(1).getName()).isEqualTo("10% 할인");
        assertThat(result.get(1).getIsUsed()).isTrue();
    }

    @Test
    @DisplayName("findMyCoupons - 보유 쿠폰이 없으면 빈 리스트를 반환한다")
    void findMyCoupons_shouldReturnEmptyList_whenNoCoupons() {
        given(userLoader.load("testuser")).willReturn(user);
        given(userCouponRepository.findAllByUserId(1L)).willReturn(List.of());

        List<FindMyCouponResponse> result = findMyCouponsService.findMyCoupons("testuser");

        assertThat(result).isEmpty();
    }
}
