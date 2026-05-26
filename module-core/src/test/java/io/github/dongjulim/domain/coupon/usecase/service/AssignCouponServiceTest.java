package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AssignCouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserCouponRepository userCouponRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssignCouponService assignCouponService;

    private Coupon coupon;
    private User user;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder().id(1L).name("할인쿠폰").discountType(DiscountType.FIXED).discountValue(1000L).build();
        user = User.builder().id(2L).username("testuser").build();
    }

    @Test
    @DisplayName("assignCoupon - 사용자에게 쿠폰이 정상적으로 지급된다")
    void assignCoupon_shouldAssignCouponToUser() {
        given(couponRepository.findById(1L)).willReturn(Optional.of(coupon));
        given(userRepository.findByIdAndDeleteCheckFalse(2L)).willReturn(Optional.of(user));

        assignCouponService.assignCoupon(1L, 2L);

        ArgumentCaptor<UserCoupon> captor = ArgumentCaptor.forClass(UserCoupon.class);
        then(userCouponRepository).should().save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(2L);
        assertThat(captor.getValue().getCouponId()).isEqualTo(1L);
        assertThat(captor.getValue().getIsUsed()).isFalse();
    }

    @Test
    @DisplayName("assignCoupon - 존재하지 않는 쿠폰이면 CouponNotFoundException을 던진다")
    void assignCoupon_throwsCouponNotFoundException_whenCouponNotFound() {
        given(couponRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> assignCouponService.assignCoupon(99L, 2L))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    @DisplayName("assignCoupon - 존재하지 않는 사용자이면 UserNotFoundException을 던진다")
    void assignCoupon_throwsUserNotFoundException_whenUserNotFound() {
        given(couponRepository.findById(1L)).willReturn(Optional.of(coupon));
        given(userRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> assignCouponService.assignCoupon(1L, 99L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
