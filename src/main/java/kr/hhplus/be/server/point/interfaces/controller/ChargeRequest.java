package kr.hhplus.be.server.point.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChargeRequest {
    private Long userId;
    private Integer amount;
}
