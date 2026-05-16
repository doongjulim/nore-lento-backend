package io.github.dongjulim.api.controller.order;

import io.github.dongjulim.domain.order.dto.FindOrderDetailResponse;
import io.github.dongjulim.domain.order.usecase.FindOrderDetailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindOrderDetailController {

    private final FindOrderDetailUseCase findOrderDetailUseCase;

    @GetMapping("/api/v2/orders/{id}")
    public ResponseEntity<FindOrderDetailResponse> findOrderDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        FindOrderDetailResponse response = findOrderDetailUseCase.findOrderDetail(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
