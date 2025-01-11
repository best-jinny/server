package kr.hhplus.be.server.common.exceptions;

public class NotEnoughException extends RuntimeException {
    public NotEnoughException(String message) {
        super(message);
    }
}
