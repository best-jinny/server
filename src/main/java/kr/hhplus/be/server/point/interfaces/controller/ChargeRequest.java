package kr.hhplus.be.server.point.interfaces.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequest {

    @NotNull
    private Long userId;

    @Positive
    private Long amount;
}
