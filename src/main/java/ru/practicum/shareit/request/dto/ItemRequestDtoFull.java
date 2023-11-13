package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestDtoFull {
    private Long id;
    @NotBlank
    private String description;
    private LocalDateTime created;
    private long requester;
    private List<ItemDtoForRequest> items;
}
