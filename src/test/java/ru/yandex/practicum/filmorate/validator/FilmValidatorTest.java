package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.yandex.practicum.filmorate.validator.utils.ValidatorTestUtils.dtoHasErrorMessage;

class FilmValidatorTest {

    @Test
    @DisplayName("1) Проверка невалидности имени и duration в dto со значениями null")
    void shouldValidateNameAndDuration() {
        Film film = getFilm(null, null, null, null);
        Assertions.assertAll(
                () -> Assertions.assertTrue(dtoHasErrorMessage(film, "name should not be null")),
                () -> Assertions.assertTrue(dtoHasErrorMessage(film, "duration should not be null")),
                () -> Assertions.assertTrue(dtoHasErrorMessage(film, "release date should not be null"))
        );
    }

    @ParameterizedTest(name = "{index}. Проверка невалидности имени \"{arguments}\"")
    @ValueSource(strings = {"", " ", "  ", "   ", "    ", "     "})
    @DisplayName("3) Проверка невалидности имени в dto с пустыми значениями")
    void shouldValidateNameToBlank(String name) {
        Film film = getFilm(name, "some description", 12, new Date());
        Assertions.assertTrue(dtoHasErrorMessage(film, "name should not be blank"));
    }

    @Test
    @DisplayName("2) Проверка невалидности описании в dto с длинным значением")
    void descriptionLengthShouldBeSmallerThsn200() {
        Film film = getFilm("name", "This is some description to film with other param and with description length greater than 200. This is some description to film with other param and with description length greater than 200, This is some description to film with other param and with description length greater than 200 ", 12, new Date());
        Assertions.assertTrue(dtoHasErrorMessage(film, "description length should not be smaller than 200"));
    }


    @Test
    @DisplayName("2) Проверка невалидности описании в dto с длинным значением")
    void releaseDateShouldBeAfterFilmBd() {
        Date realeaseDate = getDateFromString("05-10-1894");
        Film film = getFilm("name", "description", 12, realeaseDate);
        Assertions.assertTrue(dtoHasErrorMessage(film, "release date must be after 28-12-1895"));
    }


    @ParameterizedTest(name = "{index}. Проверка невалидности duration \"{arguments}\"")
    @ValueSource(ints = {Integer.MIN_VALUE, -10, -5, -1})
    @DisplayName("4) Проверка невалидности duration в dto")
    void shouldSetPositiveDuaration(Integer duration) {
        Film film = getFilm("50 shapes of gray ", "some description", duration, new Date());
        Assertions.assertTrue(dtoHasErrorMessage(film, "duration should be greater than 0"));
    }

    public static Film getFilm(String name, String description, Integer duration, Date releaseDate) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setDuration(duration);
        film.setReleaseDate(releaseDate);
        return film;
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