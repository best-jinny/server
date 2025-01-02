package kr.hhplus.be.server.product.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductRequest {
    private int page;
    private int size;
}
