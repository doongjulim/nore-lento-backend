package io.github.dongjulim.api.controller.order;

import io.github.dongjulim.domain.order.dto.SaveOrderRequest;
import io.github.dongjulim.domain.order.usecase.SaveOrderUseCase;
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
public class SaveOrderController {

    private final SaveOrderUseCase saveOrderUseCase;

    @PostMapping("/api/v2/orders")
    public ResponseEntity<Void> saveOrder(
            @RequestBody @Valid SaveOrderRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        saveOrderUseCase.saveOrder(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
