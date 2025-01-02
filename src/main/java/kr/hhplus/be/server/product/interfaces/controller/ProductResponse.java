package kr.hhplus.be.server.product.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private int price;
    private int stock;
}
