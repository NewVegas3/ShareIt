package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    // Метод для создания нового пользователя
    public UserDto createUser(UserDto dto) {
        User user = repository.save(UserMapper.toUser(dto));
        return UserMapper.toUserDto(user);
    }

    // Метод для обновления информации о пользователе
    public UserDto updateUser(UserDto dto, long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден.");
        }

        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        user.setId(userId);

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        repository.save(user);
        return UserMapper.toUserDto(user);
    }

    // Метод для удаления пользователя по ID
    public void deleteUser(long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }
        repository.deleteById(id);
    }

    // Метод для получения списка всех пользователей
    public Collection<UserDto> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    // Метод для получения информации о пользователе по ID
    public UserDto getUserById(long id) {
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден."));
        return UserMapper.toUserDto(user);
    }
}
