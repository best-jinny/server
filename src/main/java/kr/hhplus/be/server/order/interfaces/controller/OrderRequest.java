package kr.hhplus.be.server.order.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderRequest {
    private String customerId;
    private List<OrderItemRequest> orderItems;
    private String couponCode;
}
