package kr.hhplus.be.server.product.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;

}
