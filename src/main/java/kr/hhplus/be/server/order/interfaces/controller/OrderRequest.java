package kr.hhplus.be.server.order.interfaces.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull
    private Long userId;

    @NotEmpty
    private List<OrderLineRequest> orderLines;

    private Long couponId;

    public OrderRequest(Long userId, List<OrderLineRequest> orderLines) {
        this.userId = userId;
        this.orderLines = orderLines;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderLineRequest {

        @NotNull
        private Long productId;

        @NotNull
        private int quantity;

        @NotNull
        private Long price;
    }

}
