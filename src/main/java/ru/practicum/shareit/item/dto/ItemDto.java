package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(groups = {New.class, ExistUpdateDescription.class, ExistUpdateNameEmailDescAvail.class, ExistUpdateAvailable.class})
    // New.class - применимо для новых объектов Item
    private String name;
    @NotBlank(groups = {New.class, ExistUpdateName.class, ExistUpdateNameEmailDescAvail.class, ExistUpdateAvailable.class})
    private String description;
    @NotNull(groups = {New.class, ExistUpdateNameEmailDescAvail.class})
    private Boolean available; // класс-обертка может быть null
    private Long userId;
    private Long requestId;
}
