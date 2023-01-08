package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;

	@Test
	public void testFindUserById() {

		addTestUser();

		Optional<User> userOptional = userStorage.findUserById(1);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	private void addTestUser(){
		User user = new User();
		user.setId(1);
		user.setName("login");
		user.setLogin("login");
		user.setEmail("login@gmail.com");
		user.setBirthday(new Date());

		try {
			userStorage.addNewUser(user);
		} catch (ValidationException e) {
			e.printStackTrace();
		}

	}

}
