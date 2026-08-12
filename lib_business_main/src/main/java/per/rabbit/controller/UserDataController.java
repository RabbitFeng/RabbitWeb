package per.rabbit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/user")
@Slf4j
public class UserDataController {
    /**
     * 获取用户资料
     */
    @GetMapping("/info")
    public void info() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("principal : {}", principal);
    }
}
