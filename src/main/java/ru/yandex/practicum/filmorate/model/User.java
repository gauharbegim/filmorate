package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validator.UserEmailValidate;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@UserEmailValidate
@FieldDefaults
public class User {
    Integer id;

    String email;

    String login;

    String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date birthday;
}
