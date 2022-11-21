package task02.wrapper;

public class NoChangeUnmodifiableException extends RuntimeException{
    public NoChangeUnmodifiableException() {
        super();
    }

    public NoChangeUnmodifiableException(String message) {
        super(message);
    }

    public NoChangeUnmodifiableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoChangeUnmodifiableException(Throwable cause) {
        super(cause);
    }
}
