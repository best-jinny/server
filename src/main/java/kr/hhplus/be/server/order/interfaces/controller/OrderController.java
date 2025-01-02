package kr.hhplus.be.server.order.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    // 주문
    @PostMapping
    public ResponseEntity<?> order(@RequestBody OrderRequest request) {

        // 1. customerId가 비어 있으면 실패
        if (request.getCustomerId() == null || request.getCustomerId().isEmpty()) {
            return ResponseEntity.badRequest().body("customerId는 필수입니다.");
        }

        // 2. 주문 항목이 비어 있으면 실패
        if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
            return ResponseEntity.badRequest().body("주문 항목은 최소한 1개 이상이어야 합니다.");
        }

        // 3. 각 주문 항목의 amount가 0보다 커야 함
        for (OrderItemRequest item : request.getOrderItems()) {
            if (item.getAmount() <= 0) {
                return ResponseEntity.badRequest().body("주문 수량(amount)은 0보다 커야 합니다.");
            }
        }

        OrderResponse response = new OrderResponse("20241229EDEDA122", "PAYMENT_COMPLETE", 50000, 10000, 40000);

        return ResponseEntity.ok(response);
    }
}
