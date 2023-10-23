package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.Exist;
import ru.practicum.shareit.validation.ExistUpdateEmail;
import ru.practicum.shareit.validation.ExistUpdateName;
import ru.practicum.shareit.validation.New;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    @NotNull(groups = Exist.class)
    private long id;

    @NotBlank(groups = {New.class, ExistUpdateName.class})
    private String name;

    @NotBlank(groups = {New.class, ExistUpdateEmail.class})
    @Email(groups = {New.class, ExistUpdateEmail.class})
    private String email;
}