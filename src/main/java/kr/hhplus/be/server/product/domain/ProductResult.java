package kr.hhplus.be.server.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResult {
    private Long id;
    private String name;
    private Long price;
    private int stock;
}
