package kr.hhplus.be.server.order.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private String orderStatus; // todo Enum 으로 분리 필요
    private int totalAmount;
    private int discountAmount;
    private int finalAmount;
}
