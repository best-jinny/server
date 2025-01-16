package kr.hhplus.be.server.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResult {
    private Long id;
    private String name;
    private Long price;
    private int stock;
}
