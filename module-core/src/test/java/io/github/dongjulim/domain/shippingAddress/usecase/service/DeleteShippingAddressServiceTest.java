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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DeleteShippingAddressServiceTest {

    @Mock
    private ShippingAddressRepository shippingAddressRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private DeleteShippingAddressService deleteShippingAddressService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("deleteShippingAddress - 배송지가 정상적으로 삭제된다")
    void deleteShippingAddress_shouldDelete() {
        ShippingAddress address = ShippingAddress.builder()
                .id(1L).userId(1L).recipientName("홍길동").phone("010-1111-1111")
                .address("서울시 강남구").zipCode("12345").isDefault(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(address));

        deleteShippingAddressService.deleteShippingAddress(1L, "testuser");

        then(shippingAddressRepository).should().delete(address);
    }

    @Test
    @DisplayName("deleteShippingAddress - 존재하지 않거나 타인의 배송지이면 ShippingAddressNotFoundException을 던진다")
    void deleteShippingAddress_throwsShippingAddressNotFoundException_whenNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deleteShippingAddressService.deleteShippingAddress(99L, "testuser"))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }
}
