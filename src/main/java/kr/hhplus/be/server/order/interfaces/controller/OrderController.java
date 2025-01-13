package kr.hhplus.be.server.order.interfaces.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.order.domain.OrderService;
import kr.hhplus.be.server.order.domain.TopProductsResult;
import kr.hhplus.be.server.order.facade.OrderCriteria;
import kr.hhplus.be.server.order.facade.OrderFacade;
import kr.hhplus.be.server.order.facade.OrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderFacade orderFacade;
    private final OrderService orderService;

    // 주문
    @PostMapping
    public ResponseEntity<OrderResult> order(@Valid @RequestBody OrderRequest request) {

        OrderResult orderResult = orderFacade.processImmediatePayOrder(OrderCriteria.of(request));

        return ResponseEntity.ok(orderResult);
    }

    // 최근 3일간 판매량 top 5 상품 조회
    @GetMapping("/statistics/top-sellers")
    public ResponseEntity<?> getTopSellers() {
        List<TopProductsResult> topSellers = orderService.getTopProducts();
        return ResponseEntity.ok(topSellers);
    }

}
