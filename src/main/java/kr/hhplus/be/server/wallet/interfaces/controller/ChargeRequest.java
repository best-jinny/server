package kr.hhplus.be.server.wallet.interfaces.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChargeRequest {
    private Long customerId;
    private Integer amount;
}
