package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.util.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ItemMapperTest {
    private final User user = new User(1L, "test", "rest@mail.ru");

    private final ItemDto dto = new ItemDto(1L, "test", "test", true, user.getId(), null);

    @Test
    void toItem() {
        Item item = ItemMapper.toItem(dto, user, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(dto.getId(), item.getId()),
                () -> Assertions.assertEquals(dto.getName(), item.getName()),
                () -> Assertions.assertEquals(dto.getDescription(), item.getDescription()),
                () -> Assertions.assertEquals(dto.getAvailable(), item.getIsAvailable()),
                () -> Assertions.assertEquals(dto.getUserId(), item.getUser().getId())
        );
    }

    @Test
    void toItemDto() {
        Item item = new Item(1L, "test", "test", true, user, null);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        Assertions.assertAll(
                () -> Assertions.assertEquals(itemDto.getId(), item.getId()),
                () -> Assertions.assertEquals(itemDto.getName(), item.getName()),
                () -> Assertions.assertEquals(itemDto.getDescription(), item.getDescription()),
                () -> Assertions.assertEquals(itemDto.getAvailable(), item.getIsAvailable()),
                () -> Assertions.assertEquals(itemDto.getUserId(), item.getUser().getId())
        );
    }

    @Test
    void toItemDtoForBooking() {
        Item item = new Item(1L, "test", "test", true, user, null);
        ItemDtoForBooking itemDtoForBooking = ItemMapper.toItemDtoForBooking(item);

        Assertions.assertAll(
                () -> Assertions.assertEquals(itemDtoForBooking.getId(), item.getId()),
                () -> Assertions.assertEquals(itemDtoForBooking.getName(), item.getName())
        );
    }

    @Test
    void toItemDtoFullIfBothBookingsNull() {
        Item item = new Item(1L, "test", "test", true, user, new ItemRequest(1L, "test", LocalDateTime.now(), user));
        ItemDtoFull itemDtoFull = ItemMapper.toItemDtoFull(item, null, null, new ArrayList<>());

        Assertions.assertAll(
                () -> Assertions.assertEquals(itemDtoFull.getId(), item.getId()),
                () -> Assertions.assertEquals(itemDtoFull.getName(), item.getName()),
                () -> Assertions.assertEquals(itemDtoFull.getDescription(), item.getDescription()),
                () -> Assertions.assertEquals(itemDtoFull.getAvailable(), item.getIsAvailable()),
                () -> Assertions.assertEquals(itemDtoFull.getUserId(), item.getUser().getId())
        );
    }

    @Test
    void toItemDtoFullIfNextBookingIsNotNull() {
        Item item = new Item(1L, "test", "test", true, user, new ItemRequest(1L, "test", LocalDateTime.now(), user));
        Booking lastBooking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, Status.WAITING);
        ItemDtoFull itemDtoFull = ItemMapper.toItemDtoFull(item, lastBooking, null, new ArrayList<>());

        Assertions.assertAll(
                () -> Assertions.assertEquals(itemDtoFull.getLastBooking().getId(), lastBooking.getId()),
                () -> Assertions.assertEquals(itemDtoFull.getLastBooking().getBookerId(), lastBooking.getBooker().getId()),
                () -> Assertions.assertNull(itemDtoFull.getNextBooking())
        );
    }

    @Test
    void toItemDtoFullIfLastBookingsEqualsNextBooking() {
        Item item = new Item(1L, "test", "test", true, user, new ItemRequest(1L, "test", LocalDateTime.now(), user));
        Booking lastBooking = new Booking(2L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item, user, Status.WAITING);
        Booking nextBooking = lastBooking;
        ItemDtoFull itemDtoFull = ItemMapper.toItemDtoFull(item, lastBooking, nextBooking, new ArrayList<>());

        Assertions.assertAll(
                () -> Assertions.assertEquals(itemDtoFull.getLastBooking().getId(), lastBooking.getId()),
                () -> Assertions.assertEquals(itemDtoFull.getLastBooking().getBookerId(), lastBooking.getBooker().getId()),
                () -> Assertions.assertNull(itemDtoFull.getNextBooking())
        );
    }

    @Test
    void toItemDtoForRequestIfRequestIsNull() {
        Item item = new Item(1L, "test", "test", true, user, null);
        ItemDtoForRequest itemDtoForRequest = ItemMapper.toItemDtoForRequest(item);

        Assertions.assertAll(
                () -> Assertions.assertEquals(itemDtoForRequest.getId(), item.getId()),
                () -> Assertions.assertEquals(itemDtoForRequest.getName(), item.getName()),
                () -> Assertions.assertEquals(itemDtoForRequest.getDescription(), item.getDescription()),
                () -> Assertions.assertEquals(itemDtoForRequest.getAvailable(), item.getIsAvailable())
        );
    }

    @Test
    void toItemDtoForRequestIfRequestIsNotNull() {
        ItemRequest itemRequest = new ItemRequest(1L, "test", LocalDateTime.now(), user);
        Item item = new Item(1L, "test", "test", true, user, itemRequest);
        ItemDtoForRequest itemDtoForRequest = ItemMapper.toItemDtoForRequest(item);

        Assertions.assertEquals(itemDtoForRequest.getRequestId(), item.getRequest().getId());
    }
}
