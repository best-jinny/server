package kr.hhplus.be.server.common.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidException extends BusinessException {
    public InvalidException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
