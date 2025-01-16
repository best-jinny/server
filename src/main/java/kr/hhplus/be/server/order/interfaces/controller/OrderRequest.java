package kr.hhplus.be.server.order.interfaces.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
        @Positive(message = "수량은 0보다 커야 합니다")
        private int quantity;

        @NotNull
        @Positive(message = "금액은 0보다 커야 합니다")
        private Long price;
    }

}
