package ru.practicum.shareit.cooment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoFull {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
