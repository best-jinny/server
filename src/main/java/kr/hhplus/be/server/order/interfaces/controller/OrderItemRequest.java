package kr.hhplus.be.server.order.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemRequest {
    private String productId;
    private int amount;
}
