package kr.hhplus.be.server.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotEnoughException extends BusinessException {
    public NotEnoughException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
