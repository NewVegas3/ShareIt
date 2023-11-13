package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFull;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    @GetMapping
    public Collection<BookingDtoFull> getUsersBookings(@RequestParam(required = false, defaultValue = "ALL") String state, @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                       @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer size, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getUsersBookings(state, from, size, userId);
    }

    @GetMapping("/owner")
    public Collection<BookingDtoFull> getUsersItemsBookings(@RequestParam(required = false, defaultValue = "ALL") String state, @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer size, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getUsersItemsBookings(state, from, size, userId);
    }
}
