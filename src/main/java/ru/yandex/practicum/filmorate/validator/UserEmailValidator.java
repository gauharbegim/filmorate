package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class UserEmailValidator implements ConstraintValidator<UserEmailValidate, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        if (user.getEmail() == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("email should be null")
                    .addConstraintViolation();
            return false;
        }

        if (user.getLogin() == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("login should be null")
                    .addConstraintViolation();
            return false;
        }
        boolean isUserLoginCorrect = user.getLogin().matches("\\S+");

        if (!isUserLoginCorrect) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("login should not contain blank")
                    .addConstraintViolation();
            return false;
        }

        if (user.getBirthday() == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("birthday should be null")
                    .addConstraintViolation();
            return false;
        }

        if (user.getBirthday().after(new Date())) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("birthday should be after today")
                    .addConstraintViolation();
            return false;
        }

        boolean isUserEmailContainsAt = user.getEmail().contains("@");

        if (!isUserEmailContainsAt) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("email should contains @")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
