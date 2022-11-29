package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Date;

import static ru.yandex.practicum.filmorate.validator.utils.ValidatorTestUtils.dtoHasErrorMessage;

public class UserValidatorTest {

    @Test
    @DisplayName("1) Проверка невалидности email в dto без @ в значениями ")
    void emailShouldContainAt(){
        User user = getUser("Gauhar", new Date(), "gauges.begimova", "gauhar");
        Assertions.assertTrue(dtoHasErrorMessage(user, "email should contains @"));
    }


    @ParameterizedTest(name = "{index}. Проверка невалидности email \"{arguments}\"")
    @ValueSource(strings = {"", " ", "  ", "   ", "    ", "     "})
    @DisplayName("1) Проверка невалидности email в dto c пустыми значениями")
    void emailShouldNotContainsBlank(String email){
        User user = getUser("Gauhar", new Date(), email , "gauhar");
        Assertions.assertTrue(dtoHasErrorMessage(user, "email should not be blank"));
    }

    @Test
    @DisplayName("1) Проверка невалидности email в dto в значениями null ")
    void emailShouldNotBeEmpty(){
        User user = getUser("Gauhar", new Date(), null, "gauhar");
        Assertions.assertTrue(dtoHasErrorMessage(user,  "email should not be null"));
    }


    @Test
    @DisplayName("1) Проверка невалидности email, login и birthday в dto со значениями null")
    void shouldValidateAllParamsToNull() {
        User user = getUser(null, null, null, null);
        Assertions.assertAll(
                () -> Assertions.assertTrue(dtoHasErrorMessage(user, "email should not be null")),
                () -> Assertions.assertTrue(dtoHasErrorMessage(user, "birthday should not be null")),
                () -> Assertions.assertTrue(dtoHasErrorMessage(user, "login should not be null"))
        );
    }



    @ParameterizedTest(name = "{index}. Проверка невалидности email \"{arguments}\"")
    @ValueSource(strings = {"", " ", "  ", "   ", "    ", "     "})
    @DisplayName("1) Проверка невалидности email в dto c пустыми значениями")
    void loginShouldNotContainsBlank(String login){
        User user = getUser("Gauhar", new Date(), "email@mail.ru" , login);
        Assertions.assertTrue(dtoHasErrorMessage(user,  "login should not be blank"));
    }


    public static User getUser(String name, Date birthday, String email, String login) {
        User user = new User();
        user.setName(name);
        user.setBirthday(birthday);
        user.setEmail(email);
        user.setLogin(login);
        return user;
    }

}
