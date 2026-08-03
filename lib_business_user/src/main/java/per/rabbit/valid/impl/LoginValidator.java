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
        // 跨字段校验：email 和 phone 至少填写一个（单字段的非空/格式校验交给字段注解）
        boolean hasEmail = loginDTO.getEmail() != null && !loginDTO.getEmail().isBlank();
        boolean hasPhone = loginDTO.getPhone() != null && !loginDTO.getPhone().isBlank();
        if (!hasEmail && !hasPhone) {
            log.debug("loginDTO: {}", loginDTO);
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("邮箱和手机号至少填写一个")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
