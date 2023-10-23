package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFull;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Создание новой брони
    @PostMapping
    public BookingDtoFull createBooking(@RequestBody @Valid BookingDto dto, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.createBooking(dto, userId);
    }

    // Подтверждение брони или отклонение
    @PatchMapping("/{bookingId}")
    public BookingDtoFull confirmBooking(@PathVariable long bookingId, @RequestParam boolean approved, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.confirmBooking(bookingId, approved, userId);
    }

    // Получение информации о конкретной брони по её идентификатору
    @GetMapping("/{bookingId}")
    public BookingDtoFull getBooking(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    // Получение списка бронирований пользователя с возможностью фильтрации по статусу
    @GetMapping
    public Collection<BookingDtoFull> getUsersBookings(@RequestParam(required = false, defaultValue = "ALL") String state, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getUsersBookings(state, userId);
    }

    // Получение списка бронирований элементов пользователя с возможностью фильтрации по статусу
    @GetMapping("/owner")
    public Collection<BookingDtoFull> getUsersItemsBookings(@RequestParam(required = false, defaultValue = "ALL") String state, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getUsersItemsBookings(state, userId);
    }
}
