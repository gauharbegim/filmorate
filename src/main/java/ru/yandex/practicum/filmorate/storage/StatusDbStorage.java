package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Status;

import java.util.Optional;

@Component
@Slf4j
public class StatusDbStorage implements StatusStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Status getStatusById(Integer id) {
        Optional<Status> statusById = findStatusById(id);
        if (!statusById.isEmpty()) {
            return statusById.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        }
    }

    private Optional<Status> findStatusById(Integer id) {
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("SELECT * FROM STATUS WHERE ID=?", id);
        if (statusRows.next()) {
            Status status = new Status(
                    statusRows.getInt("id"),
                    statusRows.getString("name")
            );

            return Optional.of(status);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

}
