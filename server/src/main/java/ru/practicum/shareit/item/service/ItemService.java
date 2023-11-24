package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoFull;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(ItemDto dto, long userId);

    ItemDto updateItem(ItemDto dto, long itemId, long userId);

    ItemDtoFull getItemById(long id, Long userId);

    Collection<ItemDtoFull> getAllUsersItems(Long userId);

    Collection<ItemDto> findItem(String text, Long userId);

    CommentDtoFull addComment(CommentDto commentDto, long itemId, long userId);
}
