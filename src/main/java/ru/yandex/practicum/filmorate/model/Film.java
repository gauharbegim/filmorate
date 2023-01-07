package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validator.FilmReleaseDateValidate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults
public class Film {
    Integer id;

    @NotBlank(message = "name should not be blank")
    String name;

    @Size(max = 200, message = "description length should not be smaller than 200")
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "release date should not be null")
    @FilmReleaseDateValidate
    Date releaseDate;

    @Min(value = 0, message = "duration should be greater than 0")
    @NotNull(message = "duration should not be null")
    Integer duration;

    Rating mpa;

    Set<Genre> genres;

    List<User> likedUsersList = new ArrayList<>();

    Integer likeCount = 0;
}


