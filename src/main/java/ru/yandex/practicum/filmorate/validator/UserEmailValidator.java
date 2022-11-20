package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class UserEmailValidator implements ConstraintValidator<UserEmailValidate, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        boolean res = true;

        if (user.getEmail() == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("email should not be null")
                    .addConstraintViolation();
            res= false;
        }

        if (user.getEmail() != null && !user.getEmail().matches("\\S+")) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("email should not be blank")
                    .addConstraintViolation();
            res= false;
        }

        if (user.getLogin() == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("login should not be null")
                    .addConstraintViolation();
            res= false;
        }

        if (user.getLogin() != null && !user.getLogin().matches("\\S+")) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("login should not be blank")
                    .addConstraintViolation();
            res= false;
        }

        if (user.getBirthday() == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("birthday should not be null")
                    .addConstraintViolation();
            res= false;
        }

        if (user.getBirthday()!=null && user.getBirthday().after(new Date())) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("birthday should be after today")
                    .addConstraintViolation();
            res= false;
        }

        if (user.getEmail()!=null && !user.getEmail().contains("@")) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("email should contains @")
                    .addConstraintViolation();
            res= false;
        }

        return res;
    }
}
