package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoFull;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemRequestMapperTest {
    @Test
    void toItemRequestDto() {
        User user = new User(1L, "test", "test@mail.ru");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("1");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user);

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertThat(itemRequestDto.getId(), equalTo(itemRequest.getId()));
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(itemRequestDto.getCreated(), equalTo(itemRequest.getCreated()));
        assertThat(itemRequestDto.getRequester(), equalTo(itemRequest.getRequester().getId()));
    }

    @Test
    void toItemRequest() {
        User user = new User(1L, "test", "test@mail.ru");

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("description 1");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequester(user.getId());


        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);

        assertThat(itemRequest.getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequest.getCreated(), equalTo(itemRequestDto.getCreated()));
        assertThat(itemRequest.getRequester().getId(), equalTo(itemRequestDto.getRequester()));
    }

    @Test
    void toItemRequestDtoFullIfItemsNull() {
        User user = new User(1L, "test", "test@mail.ru");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("1");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user);

        ItemRequestDtoFull itemRequestDtoFull = ItemRequestMapper.toItemRequestDtoFull(itemRequest, null);

        assertThat(itemRequestDtoFull.getId(), equalTo(itemRequest.getId()));
        assertThat(itemRequestDtoFull.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(itemRequestDtoFull.getCreated(), equalTo(itemRequest.getCreated()));
        assertThat(itemRequestDtoFull.getRequester(), equalTo(itemRequest.getRequester().getId()));
        assertThat(List.of(itemRequestDtoFull.getItems()).isEmpty(), equalTo(false));
    }

    @Test
    void toItemRequestDtoFull() {
        User user = new User(1L, "test", "test@mail.ru");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("1");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user);

        Item item1 = new Item(1L, "item1", "description1", true, user, itemRequest);
        Item item2 = new Item(2L, "item2", "description2", false, user, itemRequest);
        List<Item> items = List.of(item1, item2);

        ItemRequestDtoFull itemRequestDtoFull = ItemRequestMapper.toItemRequestDtoFull(itemRequest, items);

        assertThat(itemRequestDtoFull.getId(), equalTo(itemRequest.getId()));
        assertThat(itemRequestDtoFull.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(itemRequestDtoFull.getCreated(), equalTo(itemRequest.getCreated()));
        assertThat(itemRequestDtoFull.getRequester(), equalTo(itemRequest.getRequester().getId()));
        assertThat(itemRequestDtoFull.getItems().get(0).getId(), equalTo(items.get(0).getId()));
        assertThat(itemRequestDtoFull.getItems().get(1).getId(), equalTo(items.get(1).getId()));
    }
}
