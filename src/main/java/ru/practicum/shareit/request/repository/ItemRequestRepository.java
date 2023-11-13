package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends PagingAndSortingRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(long requesterId);

    List<ItemRequest> findAllByRequesterIdNotOrderByCreatedDesc(long userId);

    List<ItemRequest> findAllByRequesterIdNot(long userId, Pageable pageable);
}