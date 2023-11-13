package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFull;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.util.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Test
    public void createBooking() {
        User owner = new User(1L, "Anastasiya", "ana@mail.ru");
        User booker = new User(2L, "notAnastasiya", "notAna@mail.ru");
        Item item = new Item(1L, "item", "description", true, owner, new ItemRequest());
        BookingDto dto = new BookingDto(1L, LocalDateTime.of(2023, 11, 6, 23, 30), LocalDateTime.of(2023, 11, 6, 23, 50), 1L, booker.getId(), Status.WAITING);
        Booking booking = new Booking(1L, LocalDateTime.of(2023, 11, 6, 23, 30), LocalDateTime.of(2023, 11, 6, 23, 50), item, booker, Status.WAITING);

        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booker));
        Mockito.when(itemRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        BookingDtoFull bookingSaved = bookingService.createBooking(dto, 2L);

        assertThat(bookingSaved.getId(), notNullValue());
        assertThat(bookingSaved.getStart(), equalTo(dto.getStart()));
        assertThat(bookingSaved.getEnd(), equalTo(dto.getEnd()));
        assertThat(bookingSaved.getItem(), equalTo(ItemMapper.toItemDtoForBooking(item)));
        assertThat(bookingSaved.getBooker(), equalTo(UserMapper.toUserDtoForBooking(booker)));

        verify(userRepository, atLeast(1)).existsById(2L);
        verify(userRepository, atLeast(1)).findById(2L);
        verify(itemRepository, times(1)).existsById(1L);
        verify(itemRepository, times(4)).findById(1L);
        verify(bookingRepository).save(any()); // Проверяем, что метод save был вызван хотя бы один раз
    }

    @Test
    public void getBooking() {
        User user = new User(1L,"Anastasiya","ana@mail.ru");
        Item item = new Item(1L, "item", "description", true, user, new ItemRequest());
        Booking booking = new Booking(1L, LocalDateTime.of(2023, 11, 6, 23, 30), LocalDateTime.of(2023, 11, 6, 23, 50), item, user, Status.WAITING);

        Mockito.when(bookingRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));

        assertThat(bookingService.getBooking(1L, 1L),equalTo(BookingMapper.toBookingDtoFull(booking, item)));

        Mockito.verify(bookingRepository, times(1)).existsById(1L);
        Mockito.verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    public void getBookingThrowsException() {
        User owner = new User(1L,"Anastasiya","ana@mail.ru");
        User renter = new User(2L,"notAnastasiya","notAna@mail.ru");
        Item item = new Item(1L, "test", "test", true, owner, new ItemRequest());
        Booking booking = new Booking(1L, LocalDateTime.of(2023, 11, 6, 23, 30), LocalDateTime.of(2023, 11, 6, 23, 50), item, renter, Status.WAITING);

        Mockito.when(bookingRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getBooking(1L, 3L));

        Mockito.verify(bookingRepository, times(1)).existsById(1L);
        Mockito.verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    public void confirmBooking() {
        User user = new User(1L,"Anastasiya","ana@mail.ru");
        Item item = new Item(1L, "item", "description", true, user, new ItemRequest());
        Booking booking = new Booking(1L, LocalDateTime.of(2023, 11, 6, 23, 30), LocalDateTime.of(2023, 11, 6, 23, 50), item, user, Status.WAITING);
        Booking bookingApproved = new Booking(1L, LocalDateTime.of(2023, 11, 6, 23, 30), LocalDateTime.of(2023, 11, 6, 23, 50), item, user, Status.APPROVED);

        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(bookingApproved);
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));

        BookingDtoFull approvedBookingDto = bookingService.confirmBooking(1L, true, 1L);

        assertThat(approvedBookingDto.getStatus(),equalTo(Status.APPROVED));

        Mockito.verify(userRepository, times(1)).existsById(1L);
        Mockito.verify(bookingRepository, times(1)).existsById(1L);
        Mockito.verify(bookingRepository, times(1)).save(booking);
        Mockito.verify(itemRepository, times(1)).findById(1L);
        Mockito.verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    public void getUsersBookings() {
        User user = new User(1L,"Anastasiya","ana@mail.ru");
        Item item = new Item(1L, "item", "description", true, user, new ItemRequest());
        Booking booking = new Booking(1L, LocalDateTime.of(2023, 11, 6, 23, 30), LocalDateTime.of(2023, 11, 6, 23, 50), item, user, Status.WAITING);
        List<Booking> bookingList = List.of(booking);

        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findAllByBookerId(Mockito.anyLong(), Mockito.any())).thenReturn(bookingList);

        Collection<BookingDtoFull> bookings = bookingService.getUsersBookings("ALL", 0, 2, 1L);

        assertThat(bookings.size(),equalTo(1));
    }

    @Test
    public void getUsersItemsBookings() {
        User owner = new User(1L,"Anastasiya","ana@mail.ru");
        User booker = new User(2L,"notAnastasiya","notAna@mail.ru");
        Item item = new Item(1L, "item", "description", true, owner, new ItemRequest());
        Booking booking = new Booking(1L, LocalDateTime.of(2023, 11, 6, 23, 30), LocalDateTime.of(2023, 11, 6, 23, 50), item, booker, Status.WAITING);
        List<Item> itemList = List.of(item);
        List<Booking> bookingList = List.of(booking);

        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.findByUserId(Mockito.anyLong())).thenReturn(itemList);
        Mockito.when(bookingRepository.findByOwner(Mockito.anyLong(), Mockito.any())).thenReturn(bookingList);

        Collection<BookingDtoFull> bookings = bookingService.getUsersItemsBookings("ALL", 0, 2, 1L);
        assertThat(bookings.size(),equalTo(1));
    }
}
