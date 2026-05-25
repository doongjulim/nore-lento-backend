package io.github.dongjulim.api.controller.order;

import io.github.dongjulim.domain.order.dto.SaveOrderFromCartRequest;
import io.github.dongjulim.domain.order.usecase.SaveOrderFromCartUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SaveOrderFromCartController {

    private final SaveOrderFromCartUseCase saveOrderFromCartUseCase;

    @PostMapping("/api/v2/orders/from-cart")
    public ResponseEntity<Void> saveOrderFromCart(
            @RequestBody @Valid SaveOrderFromCartRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        saveOrderFromCartUseCase.saveOrderFromCart(request.getShippingAddressId(), userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
