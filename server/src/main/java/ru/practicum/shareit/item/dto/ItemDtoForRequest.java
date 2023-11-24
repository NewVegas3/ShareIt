package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDtoForRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
