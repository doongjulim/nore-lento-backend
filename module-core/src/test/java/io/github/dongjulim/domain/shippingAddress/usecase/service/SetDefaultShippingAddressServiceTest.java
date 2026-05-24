package io.github.dongjulim.domain.shippingAddress.usecase.service;

import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SetDefaultShippingAddressServiceTest {

    @Mock
    private ShippingAddressRepository shippingAddressRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private SetDefaultShippingAddressService setDefaultShippingAddressService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("setDefaultShippingAddress - 기본 배송지가 정상적으로 설정된다")
    void setDefaultShippingAddress_shouldSetDefault() {
        ShippingAddress address = ShippingAddress.builder()
                .id(2L).userId(1L).recipientName("홍길동").phone("010-1111-1111")
                .address("서울시 강남구").zipCode("12345").isDefault(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(2L, 1L)).willReturn(Optional.of(address));
        given(shippingAddressRepository.findByUserIdAndIsDefaultTrue(1L)).willReturn(Optional.empty());

        setDefaultShippingAddressService.setDefaultShippingAddress(2L, "testuser");

        assertThat(address.getIsDefault()).isTrue();
    }

    @Test
    @DisplayName("setDefaultShippingAddress - 기존 기본 배송지가 있으면 해제 후 새 기본 배송지가 설정된다")
    void setDefaultShippingAddress_shouldUnsetPreviousDefault() {
        ShippingAddress prevDefault = ShippingAddress.builder()
                .id(1L).userId(1L).recipientName("기존사람").phone("010-0000-0000")
                .address("이전 주소").zipCode("00000").isDefault(true).build();
        ShippingAddress newDefault = ShippingAddress.builder()
                .id(2L).userId(1L).recipientName("홍길동").phone("010-1111-1111")
                .address("서울시 강남구").zipCode("12345").isDefault(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(2L, 1L)).willReturn(Optional.of(newDefault));
        given(shippingAddressRepository.findByUserIdAndIsDefaultTrue(1L)).willReturn(Optional.of(prevDefault));

        setDefaultShippingAddressService.setDefaultShippingAddress(2L, "testuser");

        assertThat(prevDefault.getIsDefault()).isFalse();
        assertThat(newDefault.getIsDefault()).isTrue();
    }

    @Test
    @DisplayName("setDefaultShippingAddress - 존재하지 않거나 타인의 배송지이면 ShippingAddressNotFoundException을 던진다")
    void setDefaultShippingAddress_throwsShippingAddressNotFoundException_whenNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> setDefaultShippingAddressService.setDefaultShippingAddress(99L, "testuser"))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }
}
