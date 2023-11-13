package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.util.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    @Query("select b FROM Booking as b join Item as i on b.item = i WHERE i.user.id = :userId ORDER BY b.start DESC")
    List<Booking> findByOwnerSortByStart(long userId);

    Booking getFirstByItemIdAndEndBeforeOrderByEnd(long itemId, LocalDateTime date);

    Booking getTopByItemIdAndStartAfterOrderByStart(long itemId, LocalDateTime date);

    Booking getFirstByItemIdAndStatusNotAndStartBeforeOrderByEndDesc(long itemId, Status status, LocalDateTime date);

    Booking getFirstByItemIdAndStatusNotAndStartAfterOrderByStart(long itemId, Status status, LocalDateTime date);

    Booking findFirstByBookerId(long userId);

    List<Booking> findAllByBookerId(long userId, Pageable pageable);

    @Query("select b FROM Booking as b join Item as i on b.item = i WHERE i.user.id = :userId")
    List<Booking> findByOwner(long userId, Pageable pageable);
}
