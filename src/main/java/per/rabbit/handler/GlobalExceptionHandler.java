package per.rabbit.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import per.rabbit.common.Result;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public Result<String> handleMaxSizeException(MaxUploadSizeExceededException exc){
        log.error("File too large : {}", exc.getMessage(), exc);
        return Result.failed("File too large!");
    }
}
