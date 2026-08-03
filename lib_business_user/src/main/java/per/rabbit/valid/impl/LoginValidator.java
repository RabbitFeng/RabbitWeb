package per.rabbit.valid.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import per.rabbit.dto.LoginDTO;
import per.rabbit.valid.LoginValid;

@Slf4j
public class LoginValidator implements ConstraintValidator<LoginValid, LoginDTO> {
    @Override
    public boolean isValid(LoginDTO loginDTO, ConstraintValidatorContext constraintValidatorContext) {
        // 写校验逻辑
        if(loginDTO.getEmail() == null || loginDTO.getEmail().equals("")){
            log.debug("loginDTO: {}", loginDTO);
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("邮箱不能为空")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }
        if(loginDTO.getPassword() == null || loginDTO.getPassword().equals("")){
            log.debug("loginDTO: {}", loginDTO);
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("密码不能为空")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
