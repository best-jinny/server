package kr.hhplus.be.server.order.domain;

public enum OrderStatus {
    PENDING, // 결제 대기 중
    PAID, // 결제 완료
    PREPARING, // 상품 준비 중
    SHIPPING, // 배송 중
    DELIVERED, // 배송 완료
    CANCELLED, // 주문 취소
    REFUNDED; // 환불됨
}
