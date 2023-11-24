package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoForBooking;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;


public class UserMapperTest {
    private final UserDto dto = new UserDto(1L, "test", "rest@mail.ru");

    @Test
    void toUser() {
        User user = UserMapper.toUser(dto);

        Assertions.assertEquals(dto.getId(), user.getId());
        Assertions.assertEquals(dto.getName(), user.getName());
        Assertions.assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    void toUserDto() {
        User user = UserMapper.toUser(dto);
        UserDto dto = UserMapper.toUserDto(user);

        Assertions.assertEquals(dto.getId(), user.getId());
        Assertions.assertEquals(dto.getName(), user.getName());
        Assertions.assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    void toUserDtoForBooking() {
        User user = UserMapper.toUser(dto);
        UserDtoForBooking dtoForBooking = UserMapper.toUserDtoForBooking(user);

        Assertions.assertEquals(dtoForBooking.getId(), user.getId());
        Assertions.assertEquals(dtoForBooking.getName(), user.getName());
    }
}
