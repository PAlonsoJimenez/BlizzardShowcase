package demo.servicelayer.exceptions;

public class UserException extends RuntimeException {
    private final UserExceptionTypes userExceptionType;

    public enum UserExceptionTypes {
        BAD_AUTHENTICATION, UNAUTHORIZED, FORBIDDEN, INVALID_INPUT
    }

    public UserException(UserExceptionTypes type) {
        super(generateMessage(type, ""));
        this.userExceptionType = type;
    }

    public UserException(UserExceptionTypes type, String additionalMessage) {
        super(generateMessage(type, additionalMessage));
        this.userExceptionType = type;
    }

    public UserExceptionTypes getUserExceptionType() {
        return userExceptionType;
    }

    private static String generateMessage(UserExceptionTypes type, String additionalMessage) {
        return getExceptionTypeMessage(type) + " " + additionalMessage;
    }

    private static String getExceptionTypeMessage(UserExceptionTypes type) {
        return switch (type) {
            case BAD_AUTHENTICATION -> "Invalid email or password";
            case UNAUTHORIZED -> "Bad authentication";
            case FORBIDDEN -> "User unauthorized";
            case INVALID_INPUT -> "Invalid Input";
            default -> "User Exception.";
        };
    }
}
