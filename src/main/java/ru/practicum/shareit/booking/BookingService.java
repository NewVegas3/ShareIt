package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFull;

import java.util.Collection;

public interface BookingService {
    BookingDtoFull createBooking(BookingDto dto, long userId);

    BookingDtoFull confirmBooking(long bookingId, boolean approval, long userId);

    BookingDtoFull getBooking(long bookingId, long userId);

    Collection<BookingDtoFull> getUsersBookings(String state, Integer from, Integer size, long userId);

    Collection<BookingDtoFull> getUsersItemsBookings(String state, Integer from, Integer size, long userId);

    void checkUserId(long userId);
}
