package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ItemInDto {

    private Long id;

    @NotEmpty(message = "Ошибка! Краткое название вещи не может быть пустым.")
    private String name;

    @NotEmpty(message = "Ошибка! Развёрнутое описание вещи не может быть пустым.")
    private String description;

    private Boolean available;

    private Long requestId;

}