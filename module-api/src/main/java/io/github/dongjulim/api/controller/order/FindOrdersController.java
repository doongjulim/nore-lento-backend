package io.github.dongjulim.api.controller.order;

import io.github.dongjulim.domain.order.dto.FindOrderResponse;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import io.github.dongjulim.domain.order.usecase.FindOrdersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindOrdersController {

    private final FindOrdersUseCase findOrdersUseCase;

    @GetMapping("/api/v2/orders")
    public ResponseEntity<List<FindOrderResponse>> findOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) OrderStatus status
    ) {
        List<FindOrderResponse> response = findOrdersUseCase.findOrders(userDetails.getUsername(), status);
        return ResponseEntity.ok(response);
    }
}
