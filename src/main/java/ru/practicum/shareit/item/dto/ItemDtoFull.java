package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.cooment.dto.CommentDtoFull;
import ru.practicum.shareit.validation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoFull {
    private Long id;
    @NotBlank(groups = {New.class, ExistUpdateDescription.class, ExistUpdateNameEmailDescAvail.class, ExistUpdateAvailable.class})
    private String name;
    @NotBlank(groups = {New.class, ExistUpdateName.class, ExistUpdateNameEmailDescAvail.class, ExistUpdateAvailable.class})
    private String description;
    @NotNull(groups = {New.class, ExistUpdateNameEmailDescAvail.class})
    private Boolean available; // класс-обертка может быть null
    private Long userId;
    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;
    Set<CommentDtoFull> comments;

}
