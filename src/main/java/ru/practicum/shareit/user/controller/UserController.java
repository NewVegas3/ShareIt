package ru.practicum.shareit.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Exist;
import ru.practicum.shareit.validation.New;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Создает нового пользователя и возвращает его DTO.
    @PostMapping
    public UserDto createUser(@RequestBody @Validated(New.class) UserDto dto) {
        return userService.createUser(dto);
    }

    // Обновляет имя пользователя по его ID и возвращает его обновленный DTO.
    @PatchMapping("/{userId}")
    public UserDto updateUserName(@RequestBody @Validated(Exist.class)  UserDto dto, @PathVariable long userId) {
        return userService.updateUser(dto, userId);
    }

    // Удаляет пользователя по его ID.
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

    // Возвращает список всех пользователей в виде DTO.
    @GetMapping
    public Collection<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    // Получает информацию о пользователе по его ID и возвращает его DTO.
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }
}

