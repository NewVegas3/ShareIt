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
    @NotNull(groups = Exist.class)
    private long id;

    @NotBlank(groups = {New.class, ExistUpdateDescription.class, ExistUpdateNameEmailDescAvail.class, ExistUpdateAvailable.class})
    private String name;

    @NotBlank(groups = {New.class, ExistUpdateName.class, ExistUpdateNameEmailDescAvail.class, ExistUpdateAvailable.class})
    private String description;

    @NotNull(groups = {New.class, ExistUpdateNameEmailDescAvail.class})
    private Boolean available;

    private long owner;
    private long request;
}