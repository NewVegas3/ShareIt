package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoFull;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private ItemRequestRepository itemRequestRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        checkUserId(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, userRepository.findById(userId).get());
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    public Collection<ItemRequestDtoFull> getAllUsersRequests(long userId) {
        checkUserId(userId);
        return itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId)
                .stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestDtoFull(itemRequest, itemRepository.findByRequestId(itemRequest.getId())))
                .collect(toList());
    }

    public Collection<ItemRequestDtoFull> getAllRequests(Long userId, Integer from, Integer size) {
        checkUserId(userId);
        if (from < 0) {
            throw new ValidationException("Индекс первого элемента должен быть положительным");
        }
        if (size <= 0) {
            throw new ValidationException("Количество элементов для отображения должно быть больше нуля");
        }
        Set<ItemRequestDtoFull> itemRequestDtoFullSet = new HashSet<>();
        Pageable allRequests =
                PageRequest.of(0, size, Sort.by("created").descending());
        List<ItemRequest> requests =
                itemRequestRepository.findAllByRequesterIdNot(userId, allRequests);
        itemRequestDtoFullSet
                .addAll(requests.stream().map(itemRequest -> ItemRequestMapper.toItemRequestDtoFull(itemRequest, itemRepository.findByRequestId(itemRequest.getId())))
                        .collect(toSet()));
        return itemRequestDtoFullSet;
    }

    public ItemRequestDtoFull getItemRequestById(Long userId, Long id) {
        checkUserId(userId);
        checkRequestId(id);
        ItemRequest request = itemRequestRepository.findById(id).get();
        return ItemRequestMapper.toItemRequestDtoFull(request, itemRepository.findByRequestId(request.getId()));
    }

    private void checkUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    private void checkRequestId(long requestId) {
        if (!itemRequestRepository.existsById(requestId)) {
            throw new NotFoundException("Запрос с id " + requestId + " не найден");
        }
    }
}
