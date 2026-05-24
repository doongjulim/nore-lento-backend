package io.github.dongjulim.domain.shippingAddress.usecase.service;

import io.github.dongjulim.domain.shippingAddress.dto.SaveShippingAddressRequest;
import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SaveShippingAddressServiceTest {

    @Mock
    private ShippingAddressRepository shippingAddressRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private SaveShippingAddressService saveShippingAddressService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("saveShippingAddress - 배송지가 정상적으로 저장된다")
    void saveShippingAddress_shouldSaveShippingAddress() {
        SaveShippingAddressRequest request = new SaveShippingAddressRequest();
        ReflectionTestUtils.setField(request, "recipientName", "홍길동");
        ReflectionTestUtils.setField(request, "phone", "010-1234-5678");
        ReflectionTestUtils.setField(request, "address", "서울시 강남구");
        ReflectionTestUtils.setField(request, "detailAddress", "101호");
        ReflectionTestUtils.setField(request, "zipCode", "12345");
        ReflectionTestUtils.setField(request, "isDefault", false);

        given(userLoader.load("testuser")).willReturn(user);

        saveShippingAddressService.saveShippingAddress(request, "testuser");

        ArgumentCaptor<ShippingAddress> captor = ArgumentCaptor.forClass(ShippingAddress.class);
        then(shippingAddressRepository).should().save(captor.capture());
        ShippingAddress saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo(1L);
        assertThat(saved.getRecipientName()).isEqualTo("홍길동");
        assertThat(saved.getPhone()).isEqualTo("010-1234-5678");
        assertThat(saved.getAddress()).isEqualTo("서울시 강남구");
        assertThat(saved.getDetailAddress()).isEqualTo("101호");
        assertThat(saved.getZipCode()).isEqualTo("12345");
        assertThat(saved.getIsDefault()).isFalse();
    }

    @Test
    @DisplayName("saveShippingAddress - isDefault=true로 등록 시 기존 기본 배송지가 해제된다")
    void saveShippingAddress_shouldUnsetPreviousDefault_whenNewDefaultRegistered() {
        ShippingAddress prevDefault = ShippingAddress.builder()
                .id(10L).userId(1L).recipientName("기존사람").phone("010-0000-0000")
                .address("이전 주소").zipCode("00000").isDefault(true).build();

        SaveShippingAddressRequest request = new SaveShippingAddressRequest();
        ReflectionTestUtils.setField(request, "recipientName", "홍길동");
        ReflectionTestUtils.setField(request, "phone", "010-1234-5678");
        ReflectionTestUtils.setField(request, "address", "서울시 강남구");
        ReflectionTestUtils.setField(request, "zipCode", "12345");
        ReflectionTestUtils.setField(request, "isDefault", true);

        given(userLoader.load("testuser")).willReturn(user);
        given(shippingAddressRepository.findByUserIdAndIsDefaultTrue(1L)).willReturn(Optional.of(prevDefault));

        saveShippingAddressService.saveShippingAddress(request, "testuser");

        assertThat(prevDefault.getIsDefault()).isFalse();
    }
}
