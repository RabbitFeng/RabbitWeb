package per.rabbit.exc;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }

    /**
     * 用户不存在
     */
    public static LoginException userNotExists(String message) {
        return new LoginException(message);
    }

    /**
     * 密码错误
     */
    public static LoginException passwordError(String message) {
        return new LoginException(message);
    }
}
