package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Set<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    @Query("select b FROM Booking as b join Item as i on b.item = i WHERE i.user.id = :userId ORDER BY b.start DESC")
    Set<Booking> findByOwnerSortByStart(long userId);

    Booking getFirstByItemIdAndEndBeforeOrderByEnd(long itemId, LocalDateTime date);

    Booking getTopByItemIdAndStartAfterOrderByStart(long itemId, LocalDateTime date);

    Booking getFirstByItemIdAndStatusNotAndStartBeforeOrderByEndDesc(long itemId, Status status, LocalDateTime date);

    Booking getFirstByItemIdAndStatusNotAndStartAfterOrderByStart(long itemId, Status status, LocalDateTime date);

    Booking findFirstByBookerId(long userId);
}
