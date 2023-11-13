package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto dto);

    UserDto updateUser(UserDto dto, long userId);

    void deleteUser(long id);

    Collection<UserDto> getAllUsers();

    UserDto getUserById(long id);
}
