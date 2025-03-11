package site.walkies.walkie.global.auth.exception;

public class InvalidJWTException extends RuntimeException {

    public InvalidJWTException() {}

    public InvalidJWTException(String message) {
        super(message);
    }

    public InvalidJWTException(String message, Throwable cause) {
        super(message, cause);
    }
}
