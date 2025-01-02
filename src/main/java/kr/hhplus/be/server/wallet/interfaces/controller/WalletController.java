package kr.hhplus.be.server.wallet.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    // 잔액 충전
    @PostMapping
    public ResponseEntity<?> charge(@RequestBody ChargeRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        }

        ChargeResponse response = new ChargeResponse(1L, 50000);
        return ResponseEntity.ok(response);

    }

    // 잔액 조회
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam Long customerId) {
        if (customerId == null) {
            return ResponseEntity.badRequest().body("유효하지 않은 사용자 ID 입니다.");
        }
        GetBalanceResponse response = new GetBalanceResponse(customerId, 50000);
        return ResponseEntity.ok(response);
    }


}
