package io.github.dongjulim.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class SaveOrderRequest {

    @Valid
    @NotEmpty
    private List<OrderItemRequest> orderItems;
}
