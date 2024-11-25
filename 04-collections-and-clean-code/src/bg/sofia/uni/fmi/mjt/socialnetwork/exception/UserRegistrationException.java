package bg.sofia.uni.fmi.mjt.socialnetwork.exception;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Exception e) {
        super(message, e);
    }
}
