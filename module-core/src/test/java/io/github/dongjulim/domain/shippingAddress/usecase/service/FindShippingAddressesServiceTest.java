package io.github.dongjulim.domain.shippingAddress.usecase.service;

import io.github.dongjulim.domain.shippingAddress.dto.FindShippingAddressResponse;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindShippingAddressesServiceTest {

    @Mock
    private ShippingAddressRepository shippingAddressRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindShippingAddressesService findShippingAddressesService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findShippingAddresses - 사용자의 배송지 목록을 반환한다")
    void findShippingAddresses_shouldReturnList() {
        ShippingAddress addr1 = ShippingAddress.builder()
                .id(1L).userId(1L).recipientName("홍길동").phone("010-1111-1111")
                .address("서울시 강남구").zipCode("12345").isDefault(true).build();
        ShippingAddress addr2 = ShippingAddress.builder()
                .id(2L).userId(1L).recipientName("홍길순").phone("010-2222-2222")
                .address("서울시 서초구").zipCode("67890").isDefault(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findAllByUserId(1L)).willReturn(List.of(addr1, addr2));

        List<FindShippingAddressResponse> result = findShippingAddressesService.findShippingAddresses("testuser");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getRecipientName()).isEqualTo("홍길동");
        assertThat(result.get(0).getIsDefault()).isTrue();
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getIsDefault()).isFalse();
    }

    @Test
    @DisplayName("findShippingAddresses - 배송지가 없으면 빈 목록을 반환한다")
    void findShippingAddresses_shouldReturnEmptyList_whenNone() {
        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findAllByUserId(1L)).willReturn(List.of());

        List<FindShippingAddressResponse> result = findShippingAddressesService.findShippingAddresses("testuser");

        assertThat(result).isEmpty();
    }
}
