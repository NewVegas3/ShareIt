package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoFull;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemRequestControllerTest {
    @InjectMocks
    private ItemRequestController itemRequestController;
    @Mock
    private ItemRequestService itemRequestService;

    private final User user = new User(1L, "test", "rest@mail.ru");
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "test", LocalDateTime.now(), 1L);

    @Test
    void createTest() {
        when(itemRequestService.createItemRequest(user.getId(), itemRequestDto))
                .thenReturn(itemRequestDto);

        ItemRequestDto itemRequestDto1 = itemRequestController.createItemRequest(1L, itemRequestDto);
        assertEquals(Objects.requireNonNull(itemRequestDto1).getId(), itemRequestDto.getId());

        Mockito.verify(itemRequestService, times(1)).createItemRequest(user.getId(), itemRequestDto);
    }

    @Test
    void getItemRequests() {
        Collection<ItemRequestDtoFull> collection = List.of(ItemRequestMapper.toItemRequestDtoFull(ItemRequestMapper.toItemRequest(itemRequestDto, user), null));
        when(itemRequestService.getAllUsersRequests(1L))
                .thenReturn(collection);

        Collection<ItemRequestDtoFull> list = itemRequestController.getAllUsersRequest(1L);
        assertEquals(1, Objects.requireNonNull(list).size());

        Mockito.verify(itemRequestService, times(1)).getAllUsersRequests(1L);
    }
}
