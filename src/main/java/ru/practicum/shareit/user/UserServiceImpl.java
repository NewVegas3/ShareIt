package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;
    private final List<String> emails = new ArrayList<>();

    // Создает нового пользователя и добавляет его в коллекцию пользователей.
    public UserDto createUser(UserDto dto) {
        validateEmail(dto.getEmail()); // Проверяет уникальность email.
        dto.setId(nextId); // Устанавливает уникальный id для пользователя.
        nextId++;
        User user = UserMapper.toUser(dto);
        users.put(user.getId(), user); // Добавляет пользователя в коллекцию.
        emails.add(user.getEmail()); // Добавляет email в список уникальных email.
        return dto;
    }

    // Обновляет информацию о пользователе (имя и/или email).
    public UserDto updateUser(UserDto dto, long userId) {
        UserDto userUpdate = getUserById(userId); // Получает существующего пользователя.
        if (dto.getName() != null) {
            userUpdate.setName(dto.getName()); // Обновляет имя пользователя.
        }
        if (dto.getEmail() != null) {
            checkEmail(dto.getEmail()); // Проверяет уникальность нового email.
            emails.remove(userUpdate.getEmail()); // Удаляет старый email из списка уникальных email.
            userUpdate.setEmail(dto.getEmail()); // Обновляет email пользователя.
        }
        // Не обновляем поле "id", так как оно неизменное.
        users.put(userId, UserMapper.toUser(userUpdate)); // Обновляет информацию о пользователе в коллекции.
        return userUpdate;
    }

    // Удаляет пользователя по его id.
    public void deleteUser(long id) {
        UserDto dto = getUserById(id); // Получает существующего пользователя.
        String email = dto.getEmail();
        emails.remove(email); // Удаляет email пользователя из списка уникальных email.
        users.remove(id); // Удаляет пользователя из коллекции.
    }

    // Возвращает список всех пользователей в виде DTO объектов.
    public Collection<UserDto> getAllUsers() {
        return users.values()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList()); // Преобразует список пользователей в список DTO объектов.
    }

    // Получает пользователя по его id и возвращает его в виде DTO объекта.
    public UserDto getUserById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return UserMapper.toUserDto(user);
    }

    // Проверяет уникальность email перед созданием нового пользователя.
    private void validateEmail(String email) {
        if (emails.contains(email)) {
            throw new ValidationException("Пользователь с email " + email + " уже зарегистрирован");
        }
    }

    // Проверяет уникальность email перед обновлением пользователя.
    private void checkEmail(String email) {
        if (emails.contains(email)) {
            throw new ValidationException("Пользователь с email " + email + " уже зарегистрирован");
        }
    }
}
