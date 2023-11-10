package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoForItem {
    private long id;
    private long bookerId;
}
