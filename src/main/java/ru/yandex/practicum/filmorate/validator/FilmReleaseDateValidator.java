package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class FilmReleaseDateValidator implements ConstraintValidator<FilmReleaseDateValidate, Date> {
    private static final  String ERROR_TO_TOO_OLD_DURATON = "release date must be after 28-12-1895";

    @Override
    public boolean isValid(Date releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        if (releaseDate!=null) {
            Date filmBirthDate = getDateFromString("28-12-1895");
            boolean isReleaseDateIsTooEarly = releaseDate.before(filmBirthDate);

            if (isReleaseDateIsTooEarly) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(ERROR_TO_TOO_OLD_DURATON)
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }


    private Date getDateFromString(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
