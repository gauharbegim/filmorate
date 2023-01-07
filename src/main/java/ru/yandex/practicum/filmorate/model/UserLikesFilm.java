package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserLikesFilm {
    Integer id;
    Integer filmId;
    Integer userId;
}
