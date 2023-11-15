package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFull;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongAccessException;
import ru.practicum.shareit.exception.WrongStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    // Метод для создания новой брони
    @Transactional
    public BookingDtoFull createBooking(BookingDto dto, long userId) {
        // Проверка наличия пользователя в базе данных
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        // Проверка наличия предмета в базе данных
        if (!itemRepository.existsById(dto.getItemId())) {
            throw new NotFoundException("Предмет с id " + dto.getItemId() + " не найден");
        }

        // Проверка доступности предмета
        if (!itemRepository.findById(dto.getItemId()).get().getIsAvailable()) {
            throw new WrongAccessException("Предмет с id " + dto.getItemId() + " недоступен");
        }

        // Проверка на попытку бронирования своего собственного предмета
        if (itemRepository.findById(dto.getItemId()).get().getUser().getId() == userId) {
            throw new NotFoundException("Нельзя арендовать свой предмет");
        }

        // Проверка корректности дат начала и окончания бронирования
        if (dto.getStart().isAfter(dto.getEnd()) || dto.getStart().isEqual(dto.getEnd())) {
            throw new WrongAccessException("Дата окончания бронирования не может быть позднее даты начала");
        }

        // Установка пользователя-заказчика и создание объекта Booking
        dto.setBooker(userId);
        Booking booking = BookingMapper.toBooking(dto, itemRepository.findById(dto.getItemId()).get(), userRepository.findById(userId).get());

        // Установка статуса бронирования на ожидание и сохранение в репозитории
        booking.setStatus(Status.WAITING);
        booking = bookingRepository.save(booking);

        // Получение информации о предмете
        Item item = itemRepository.findById(dto.getItemId()).get();

        // Возврат полной информации о созданной брони
        return BookingMapper.toBookingDtoFull(booking, item);
    }

    // Метод для подтверждения брони или отклонения
    @Transactional
    public BookingDtoFull confirmBooking(long bookingId, boolean approved, long userId) {
        // Проверка наличия пользователя в базе данных
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        // Проверка наличия брони в базе данных
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException("Букинг с id " + bookingId + " не найден");
        }

        // Получение информации о бронировании
        Booking booking = bookingRepository.findById(bookingId).get();

        // Проверка прав доступа: только владелец предмета может подтверждать/отклонять
        if (booking.getItem().getUser().getId() != userId) {
            throw new NotFoundException("Недостаточно прав для показа");
        }

        // Проверка статуса бронирования и возможность подтверждения/отклонения
        if (booking.getStatus().equals(Status.APPROVED) && approved || (booking.getStatus().equals(Status.REJECTED) && !approved)) {
            throw new WrongAccessException("Букинг уже подтвержден");
        }

        // Если бронирование находится в статусе "ожидание", обновляем его статус
        if (booking.getStatus().equals(Status.WAITING)) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        }

        // Сохранение обновленной информации о бронировании
        booking = bookingRepository.save(booking);

        // Получение информации о предмете
        Item item = itemRepository.findById(booking.getItem().getId()).get();

        // Возврат обновленной информации о бронировании
        return BookingMapper.toBookingDtoFull(booking, item);
    }

    // Метод для получения информации о конкретной брони
    @Transactional
    public BookingDtoFull getBooking(long bookingId, long userId) {
        if (bookingRepository.existsById(bookingId) == false) {
            throw new NotFoundException("Букинг с id " + bookingId + " не найден");
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        if (booking.getBooker().getId() == userId || booking.getItem().getUser().getId() == userId) {
            return BookingMapper.toBookingDtoFull(booking, booking.getItem());
        } else {
            throw new NotFoundException("Недостаточно прав для показа");
        }
    }

    @Transactional
    public Collection<BookingDtoFull> getUsersBookings(String state, Integer from, Integer size, long userId) {
        checkUserId(userId);
        if (from >= 1) {
            from = from - 2;
        }
        Pageable allBookings =
                PageRequest.of(from, size, Sort.by("start").descending());
        List<Booking> bookings = bookingRepository.findAllByBookerId(userId, allBookings);
        if (state.equals("ALL")) {
            return bookings
                    .stream()
                    .map(booking -> BookingMapper.toBookingDtoFull(booking, itemRepository.findById(booking.getItem().getId()).get()))
                    .collect(Collectors.toList());
        } else {
            return bookings
                    .stream()
                    .filter(getOperation(state))
                    .map(booking -> BookingMapper.toBookingDtoFull(booking, itemRepository.findById(booking.getItem().getId()).get()))
                    .collect(Collectors.toList());
        }
    }

    public Collection<BookingDtoFull> getUsersItemsBookings(String state, Integer from, Integer size, long userId) {
        checkUserId(userId);
        if (itemRepository.findByUserId(userId).size() == 0) {
            throw new NotFoundException("У пользователя с id " + userId + " нет вещей");
        }
        Pageable allBookings =
                PageRequest.of(from, size, Sort.by("start").descending());
        List<Booking> bookings = bookingRepository.findByOwner(userId, allBookings);
        if (state.equals("ALL")) {
            return bookings
                    .stream()
                    .map(booking -> BookingMapper.toBookingDtoFull(booking, itemRepository.findById(booking.getItem().getId()).get()))
                    .collect(Collectors.toList());
        } else {
            return bookings
                    .stream()
                    .filter(getOperation(state))
                    .map(booking -> BookingMapper.toBookingDtoFull(booking, itemRepository.findById(booking.getItem().getId()).get()))
                    .collect(Collectors.toList());
        }
    }

    // Метод для определения предиката фильтрации по статусу
    private Predicate<Booking> getOperation(String state) {
        switch (state) {
            case "CURRENT":
                return (booking) -> booking.getEnd().isAfter(LocalDateTime.now()) && booking.getStart().isBefore(LocalDateTime.now());
            case "PAST":
                return (booking) -> booking.getEnd().isBefore(LocalDateTime.now());
            case "FUTURE":
                return (booking) -> booking.getStart().isAfter(LocalDateTime.now());
            case "WAITING":
                return (booking) -> booking.getStatus() == Status.WAITING;
            case "REJECTED":
                return (booking) -> booking.getStatus() == Status.REJECTED;
            default:
                throw new WrongStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    // Метод для проверки существования пользователя по идентификатору
    @Transactional
    public void checkUserId(long userId) {
        // Проверка наличия пользователя в базе данных
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    // Метод для получения статуса брони на основе подтверждения
    @Transactional
    public Status getStatusFromApproved(Boolean approved) {
        // Определение статуса бронирования на основе подтверждения
        if (approved) {
            return Status.APPROVED;
        } else {
            return Status.REJECTED;
        }
    }
}
