package kr.hhplus.be.server.product.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    // 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) { // todo 상품 아이디는 Long? String?
        ProductResponse response = new ProductResponse("p1", "상품1", 10000, 100);
        return ResponseEntity.ok(response);
    }

    // 다건 조회
    @GetMapping
    public ResponseEntity<?> getProducts(ProductRequest productRequest) {

        if (productRequest.getPage() < 0 || productRequest.getSize() <= 0) {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        }

        // todo page size 최대값 체크 ?

        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.add(new ProductResponse("A1", "상품A", 2000, 100));
        productResponses.add(new ProductResponse("B1", "상품B", 5000, 0));

        ProductListResponse response = new ProductListResponse(productResponses, 2, 1, productRequest.getPage(), productRequest.getSize());
        return ResponseEntity.ok(response);
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
