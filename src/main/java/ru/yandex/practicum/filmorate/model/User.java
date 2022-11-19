package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validator.UserEmailValidate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString
public class User {
    private int id;

    @NotBlank(message = "email should not be blank")
    @NotNull(message = "email should not be null")
    @UserEmailValidate
    private String email;

    @NotBlank(message = "login should not be blank")
    @NotNull(message = "login should not be null")
    private String login;

    private String name;

    @NotNull(message = "birthday should not be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

}
