package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(ItemDto dto, long userId);

    ItemDto updateItem(ItemDto dto, long itemId,  long userId);

    ItemDto getItemById(long id, long userId);

    Collection<ItemDto> getAllUsersItems(long userId);

    Collection<ItemDto> findItem(String text, long userId);
}
