package per.rabbit.handler;

import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import per.rabbit.common.Result;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> handleException(Exception exc) {
        log.error("Exception : {}", exc.getMessage(), exc);
        return Result.failed("Internal error!");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public Result<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        log.error("File too large : {}", exc.getMessage(), exc);
        return Result.failed("File too large!");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result<String> handleIllegalArgumentException(IllegalArgumentException exc) {
        log.error("Illegal argument : {}", exc.getMessage());
        return Result.failed("Illegal argument! " + exc.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<String> handleValidationExceptionException(MethodArgumentNotValidException exc) {
        HashMap<String, String> errs = new HashMap<>();
        exc.getBindingResult().getAllErrors().forEach(error -> {
            errs.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        log.error("Invalid Param: {}", exc.getMessage());
        return Result.failed(errs.toString());
    }


}
