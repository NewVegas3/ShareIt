package ru.practicum.shareit.request.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class ItemRequestDto {

    private Long id;

    @NotNull(message = "У запроса вещи должно быть описание.")
    private String description;

    private LocalDateTime created;

    private long requester;

}