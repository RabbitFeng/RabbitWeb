package per.rabbit.controller;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import per.rabbit.common.Result;
import per.rabbit.dto.UserRegisterDTO;
import per.rabbit.dto.LoginDTO;
import per.rabbit.dto.LoginVO;
import per.rabbit.service.AccountService;
import per.rabbit.valid.LoginValid;

import javax.security.auth.login.AccountException;

@RestController
@RequestMapping(path = "/api/account")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    public AccountService accountService;

    @PostMapping(path = "/login", produces = "application/json")
    public Result<LoginVO> login(@LoginValid @RequestBody LoginDTO loginDTO) {
        LoginVO login = accountService.login(loginDTO);
        return Result.success(login);
    }

    /**
     * 注册接口
     * @param userRegisterDTO
     * @return
     * @throws AccountException
     */
    @PostMapping(path = "/register", produces = "application/json")
    public Result<String> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) throws AccountException {
        accountService.register(userRegisterDTO);
        return Result.success("注册成功");
    }

//    @RequestMapping(path = "/testauth", method = RequestMethod.POST, produces = "application/json")
//    public Result<String> testauth(@RequestHeader(name = "TOKEN") String token) {
//        Utils.log("LoginController", "testauth: " + token);
//        String userId = loginService.getUserId(token);
//        if(userId == null || userId.isEmpty()){
//            return NewResult.failed("Token无效!").setErrno(401);
//        }
//
//        return NewResult.success("");
//    }
}
