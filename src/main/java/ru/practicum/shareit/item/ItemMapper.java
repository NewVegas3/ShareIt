package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.cooment.dto.CommentDtoFull;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

@Component
public class ItemMapper {
    public static Item toItem(ItemDto dto, User user) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .isAvailable(dto.getAvailable())
                .user(user)
                .build();
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .userId(item.getUser().getId())
                .build();
    }

    public static ItemDtoForBooking toItemDtoForBooking(Item item) {
        return ItemDtoForBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    public static ItemDtoFull toItemDtoFull(Item item, Booking lastBooking, Booking nextBooking, Set<CommentDtoFull> comments) {
        ItemDtoFull itemDtoFull = ItemDtoFull.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .userId(item.getUser().getId())
                .comments(comments)
                .build();
        if (lastBooking != null) {
            itemDtoFull.setLastBooking(BookingMapper.toBookingDtoForItem(lastBooking));
        }
        if (nextBooking != null && !nextBooking.equals(lastBooking)) {
            itemDtoFull.setNextBooking(BookingMapper.toBookingDtoForItem(nextBooking));
        }
        return itemDtoFull;
    }
}
