package kr.hhplus.be.server.product.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopSellerResponse {
    private String productId;
    private String productName;
    private int salesCount;
}
