package ru.yandex.practicum.filmorate.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserEmailValidator.class)
public @interface UserEmailValidate {
    String message() default "error user email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
