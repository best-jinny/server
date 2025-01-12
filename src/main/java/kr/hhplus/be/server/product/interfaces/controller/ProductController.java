package kr.hhplus.be.server.product.interfaces.controller;

import kr.hhplus.be.server.common.dto.PageResponse;
import kr.hhplus.be.server.order.domain.OrderService;
import kr.hhplus.be.server.product.domain.ProductResult;
import kr.hhplus.be.server.product.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final OrderService orderService;

    // 전체 상품 조회
    @GetMapping()
    public ResponseEntity<?> allProducts(@RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<ProductResult> products = productService.getProducts(page, size);
        PageResponse<ProductResult> pageResponse = PageResponse.of(products);
        return ResponseEntity.ok(pageResponse);
    }
}
