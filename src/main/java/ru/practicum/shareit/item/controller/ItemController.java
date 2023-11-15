package ru.practicum.shareit.item.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoFull;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Exist;
import ru.practicum.shareit.validation.New;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Метод для создания нового предмета
    @PostMapping
    public ItemDto createItem(@RequestBody @Validated(New.class) ItemDto dto, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.createItem(dto, userId);
    }

    // Метод для обновления информации о предмете
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody @Validated(Exist.class) ItemDto dto, @PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(dto, itemId, userId);
    }

    // Метод для получения информации о предмете по его ID
    @GetMapping("/{itemId}")
    public ItemDtoFull getItemById(@PathVariable long itemId, @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    // Метод для получения списка всех предметов пользователя
    @GetMapping
    public Collection<ItemDtoFull> getAllUsersItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        return itemService.getAllUsersItems(userId);
    }

    // Метод для поиска предметов по текстовому описанию
    @GetMapping("/search")
    public Collection<ItemDto> findItem(@RequestParam String text, @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        return itemService.findItem(text, userId);
    }

    // Метод для добавления комментария к предмету
    @PostMapping("/{itemId}/comment")
    public CommentDtoFull addComment(@RequestBody @Valid CommentDto commentDto, @PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addComment(commentDto, itemId, userId);
    }
}
