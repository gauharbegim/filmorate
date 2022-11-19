package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEmailValidator implements ConstraintValidator<UserEmailValidate, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email!=null) {
            boolean isUserEmailContainsAt = email.contains("@");
            if (!isUserEmailContainsAt) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("email should contains @")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
