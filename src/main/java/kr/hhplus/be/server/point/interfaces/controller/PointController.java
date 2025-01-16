package kr.hhplus.be.server.point.interfaces.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.point.domain.PointResult;
import kr.hhplus.be.server.point.facade.PointFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointController {

    private final PointFacade pointFacade;

    // 잔액 충전
    @PostMapping
    public ResponseEntity<?> charge(@Valid @RequestBody ChargeRequest request) {
        PointResult result = pointFacade.charge(request.getUserId(), request.getAmount());
        ChargeResponse response = new ChargeResponse(result.getPoint());
        return ResponseEntity.ok(response);
    }

    // 잔액 조회
    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> getBalance(@PathVariable("userId") Long userId) {
        PointResult result = pointFacade.getBalance(userId);
        GetBalanceResponse response = new GetBalanceResponse(result.getPoint());
        return ResponseEntity.ok(response);
    }


}
