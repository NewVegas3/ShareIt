package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoFull;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto);

    Collection<ItemRequestDtoFull> getAllUsersRequests(long userId);

    Collection<ItemRequestDtoFull> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestDtoFull getItemRequestById(Long userId, Long id);

}
