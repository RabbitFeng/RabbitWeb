package per.rabbit.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import per.rabbit.valid.impl.LoginValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
public @interface LoginValid {
    String message() default "登录参数校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
