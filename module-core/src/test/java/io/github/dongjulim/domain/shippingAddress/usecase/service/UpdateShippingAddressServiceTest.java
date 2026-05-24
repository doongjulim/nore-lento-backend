package io.github.dongjulim.domain.shippingAddress.usecase.service;

import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.shippingAddress.dto.UpdateShippingAddressRequest;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateShippingAddressServiceTest {

    @Mock
    private ShippingAddressRepository shippingAddressRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private UpdateShippingAddressService updateShippingAddressService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("updateShippingAddress - 배송지 정보가 정상적으로 수정된다")
    void updateShippingAddress_shouldUpdateFields() {
        ShippingAddress address = ShippingAddress.builder()
                .id(1L).userId(1L).recipientName("홍길동").phone("010-1111-1111")
                .address("서울시 강남구").detailAddress("101호").zipCode("12345").isDefault(false).build();

        UpdateShippingAddressRequest request = new UpdateShippingAddressRequest();
        ReflectionTestUtils.setField(request, "recipientName", "김철수");
        ReflectionTestUtils.setField(request, "phone", "010-9999-9999");
        ReflectionTestUtils.setField(request, "address", "부산시 해운대구");
        ReflectionTestUtils.setField(request, "detailAddress", "202호");
        ReflectionTestUtils.setField(request, "zipCode", "99999");

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(address));

        updateShippingAddressService.updateShippingAddress(1L, request, "testuser");

        assertThat(address.getRecipientName()).isEqualTo("김철수");
        assertThat(address.getPhone()).isEqualTo("010-9999-9999");
        assertThat(address.getAddress()).isEqualTo("부산시 해운대구");
        assertThat(address.getDetailAddress()).isEqualTo("202호");
        assertThat(address.getZipCode()).isEqualTo("99999");
    }

    @Test
    @DisplayName("updateShippingAddress - 존재하지 않거나 타인의 배송지이면 ShippingAddressNotFoundException을 던진다")
    void updateShippingAddress_throwsShippingAddressNotFoundException_whenNotFound() {
        UpdateShippingAddressRequest request = new UpdateShippingAddressRequest();
        ReflectionTestUtils.setField(request, "recipientName", "김철수");
        ReflectionTestUtils.setField(request, "phone", "010-9999-9999");
        ReflectionTestUtils.setField(request, "address", "부산시");
        ReflectionTestUtils.setField(request, "zipCode", "99999");

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateShippingAddressService.updateShippingAddress(99L, request, "testuser"))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }
}
