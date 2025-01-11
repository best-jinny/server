package kr.hhplus.be.server.common.exceptions;

public class InvalidException extends RuntimeException {
    public InvalidException(String message) {
        super(message);
    }
}
