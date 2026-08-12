package per.rabbit.exc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import per.rabbit.common.Result;

import javax.security.auth.login.AccountException;

/**
 * 用户模块业务异常处理。
 * 只负责本模块的业务异常，通用/框架异常仍由 lib_common 的兜底 Advice 处理。
 */
@RestControllerAdvice
public class UserExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(UserExceptionHandler.class);

    @ExceptionHandler(AccountException.class)
    @ResponseBody
    public Result<String> handleAccountException(AccountException exc) {
        log.error("Account : {}", exc.getMessage());
        return Result.failed(exc.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    @ResponseBody
    public Result<String> handleLoginException(LoginException exc) {
        log.error("Login : {}", exc.getMessage());
        return Result.failed(exc.getMessage());
    }
}
