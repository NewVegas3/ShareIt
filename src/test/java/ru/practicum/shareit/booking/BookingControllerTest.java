package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFull;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.util.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingService bookingService;

    private final User user = new User(1L, "test", "rest@mail.ru");

    private final Item item = new Item(1L, "test", "test", true, user, null);

    private final BookingDto bookingDto1 = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L,
            null, Status.WAITING);

    private final BookingDto bookingDto2 = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
            1L, null, Status.APPROVED);

    @Test
    public void createBooking() {
        when(bookingService.createBooking(bookingDto1, 1L))
                .thenReturn(BookingMapper.toBookingDtoFull(BookingMapper.toBooking(bookingDto2, item, user), item));

        BookingDtoFull bookingDtoFull = bookingController.createBooking(bookingDto1, 1L);
        assertThat(Objects.requireNonNull(bookingDto1.getId())).isEqualTo(1);

        Mockito.verify(bookingService, times(1)).createBooking(bookingDto1, 1L);
    }

    @Test
    public void confirm() {
        when(bookingService.confirmBooking(1L, true, 1L))
                .thenReturn(BookingMapper.toBookingDtoFull(BookingMapper.toBooking(bookingDto2, item, user), item));

        BookingDtoFull bookingDtoFull = bookingController.confirmBooking(1L, true, 1L);
        assertThat(Objects.requireNonNull(bookingDtoFull).getStatus()).isEqualTo(Status.APPROVED);

        Mockito.verify(bookingService, times(1)).confirmBooking(1L, true, 1L);
    }

    @Test
    public void getBooking() {
        when(bookingService.getBooking(1L, 1L))
                .thenReturn(BookingMapper.toBookingDtoFull(BookingMapper.toBooking(bookingDto2, item, user), item));

        BookingDtoFull bookingDtoFull = bookingController.getBooking(1L, 1L);
        assertThat(Objects.requireNonNull(bookingDtoFull.getId())).isEqualTo(1);

        Mockito.verify(bookingService, times(1)).getBooking(1L, 1L);
    }

    @Test
    void getAllForBooker() {
        Collection collection = List.of(BookingMapper.toBookingDtoFull(BookingMapper.toBooking(bookingDto2, item, user), item));
        when(bookingService.getUsersBookings("ALL", 0, 5, 1L))
                .thenReturn(collection);

        Collection<BookingDtoFull> bookingDtos = bookingController.getUsersBookings("ALL", 0, 5, 1L);
        assertThat(Objects.requireNonNull(bookingDtos.size())).isEqualTo(1);

        Mockito.verify(bookingService, times(1)).getUsersBookings("ALL", 0, 5, 1L);
    }
}