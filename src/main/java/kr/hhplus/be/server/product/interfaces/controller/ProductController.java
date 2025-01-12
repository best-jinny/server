package kr.hhplus.be.server.product.interfaces.controller;

import kr.hhplus.be.server.common.dto.PageResponse;
import kr.hhplus.be.server.coupon.domain.IssuedCouponResult;
import kr.hhplus.be.server.product.domain.ProductResult;
import kr.hhplus.be.server.product.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    // 전체 상품 조회
    @GetMapping()
    public ResponseEntity<?> allProducts(@RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<ProductResult> products = productService.getProducts(page, size);
        PageResponse<ProductResult> pageResponse = PageResponse.of(products);
        return ResponseEntity.ok(pageResponse);
    }

    // 최근 3일간 판매량 top 5 상품 조회
    @GetMapping("/top-sellers")
    public ResponseEntity<?> getTopSellers() {
        List<TopSellerResponse> topSellers = new ArrayList<>();

        topSellers.add(new TopSellerResponse("p1", "상품A", 500));
        topSellers.add(new TopSellerResponse("p34", "상품B", 350));
        topSellers.add(new TopSellerResponse("p23", "상품C", 210));
        topSellers.add(new TopSellerResponse("p15", "상품D", 180));
        topSellers.add(new TopSellerResponse("p9", "상품E", 150));

        return ResponseEntity.ok(topSellers);
    }
}
