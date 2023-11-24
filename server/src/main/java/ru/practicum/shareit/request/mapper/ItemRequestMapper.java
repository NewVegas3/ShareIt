package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoFull;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requester(itemRequest.getRequester().getId())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .requester(user)
                .build();
    }

    public static ItemRequestDtoFull toItemRequestDtoFull(ItemRequest itemRequest, List<Item> items) {
        List<ItemDtoForRequest> requestsItemsSet = new ArrayList<>();
        if (items != null) {
            requestsItemsSet = items
                    .stream()
                    .map(ItemMapper::toItemDtoForRequest)
                    .collect(Collectors.toList());
        }

        return ItemRequestDtoFull.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requester(itemRequest.getRequester().getId())
                .items(requestsItemsSet)
                .build();
    }
}
