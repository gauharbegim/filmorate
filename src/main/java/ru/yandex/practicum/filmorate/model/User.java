package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validator.UserEmailValidate;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@UserEmailValidate
public class User {
    private Integer id;

    private String email;

    private String login;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

//    private Set<Friend> friends;
}
