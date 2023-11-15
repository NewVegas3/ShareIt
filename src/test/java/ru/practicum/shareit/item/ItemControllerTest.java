package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoFull;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemService itemService;

    private final User user = new User(1L, "test", "rest@mail.ru");

    private final ItemDto itemDto1 = new ItemDto(0L, "item_name", "item_description", true, 1L, null);
    private final ItemDto itemDto2 = new ItemDto(1L, "item_name", "item_description", true, 1L, null);
    private final ItemDto itemDto3 = new ItemDto(1L, "update_name", "update_description", true, 1L, null);

    private final CommentDto commentDto = new CommentDto(1L, "text", 1L, 1L, LocalDateTime.now());

    private final CommentDtoFull commentDtoFull = new CommentDtoFull(1L, "text", "name", LocalDateTime.now());

    @Test
    void createItem() {
        when(itemService.createItem(itemDto1, user.getId()))
                .thenReturn(itemDto2);

        ItemDto itemDto = itemController.createItem(itemDto1, 1L);

        assertEquals(Objects.requireNonNull(itemDto).getId(), itemDto2.getId());

        // Проверяем, что метод createItem был вызван с определенными параметрами
        verify(itemService).createItem(itemDto1, user.getId());
    }

    @Test
    void updateItem() {
        when(itemService.updateItem(eq(itemDto3), eq(1L), eq(1L)))
                .thenReturn(itemDto3);

        ItemDto itemDto = itemController.updateItem(itemDto3, 1L, 1L);

        assertEquals("update_name", Objects.requireNonNull(itemDto).getName());
        assertEquals("update_description", itemDto.getDescription());
        verify(itemService).updateItem(eq(itemDto3), eq(1L), eq(1L));
    }

    @Test
    void getItems() {
        Collection<ItemDtoFull> collection = List.of(ItemMapper.toItemDtoFull(ItemMapper.toItem(itemDto2, user, null), null, null, null));
        when(itemService.getAllUsersItems(eq(1L)))
                .thenReturn(collection);

        Collection<ItemDtoFull> list = itemController.getAllUsersItems(1L);
        assertEquals(1, Objects.requireNonNull(list).size());
        verify(itemService).getAllUsersItems(eq(1L));
    }

    @Test
    void findItem() {
        when(itemService.findItem(eq("uPd"), eq(1L)))
                .thenReturn(List.of(itemDto3));

        Collection<ItemDto> list = itemController.findItem("uPd", 1L);
        assertEquals(1, Objects.requireNonNull(list.size()));
        verify(itemService).findItem(eq("uPd"), eq(1L));
    }

    @Test
    void addComment() {
        when(itemService.addComment(commentDto, 1L, 1L))
                .thenReturn(commentDtoFull);
        CommentDtoFull commentDtoFull = itemController.addComment(commentDto, 1L, 1L);
        assertEquals(1L, Objects.requireNonNull(commentDto.getId()));
        assertEquals(1L, commentDto.getAuthor());
    }
}
