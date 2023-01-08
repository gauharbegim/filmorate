package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Status;

public interface StatusStorage {
    Status getStatusById(Integer id);
}
