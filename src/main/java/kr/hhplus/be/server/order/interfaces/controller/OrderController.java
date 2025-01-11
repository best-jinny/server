package kr.hhplus.be.server.order.interfaces.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.order.facade.OrderCriteria;
import kr.hhplus.be.server.order.facade.OrderFacade;
import kr.hhplus.be.server.order.facade.OrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    // 주문
    @PostMapping
    public ResponseEntity<OrderResult> order(@Valid @RequestBody OrderRequest request) {

        OrderResult orderResult = orderFacade.processImmediatePayOrder(OrderCriteria.of(request));

        return ResponseEntity.ok(orderResult);
    }
}
