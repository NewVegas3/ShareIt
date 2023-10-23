package ru.practicum.shareit.item;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Exist;
import ru.practicum.shareit.validation.New;

import java.util.Collection;

@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Создает новый предмет и возвращает его DTO.
    @PostMapping
    public ItemDto createItem(@RequestBody @Validated(New.class) ItemDto dto, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.createItem(dto, userId);
    }

    // Обновляет информацию о предмете по его ID и возвращает его обновленный DTO.
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody @Validated(Exist.class) ItemDto dto, @PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(dto, itemId, userId);
    }

    // Получает информацию о предмете по его ID и возвращает его DTO.
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemById(itemId, userId);
    }

    // Получает список всех предметов, принадлежащих пользователю, в виде DTO.
    @GetMapping
    public Collection<ItemDto> getAllUsersItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllUsersItems(userId);
    }

    // Ищет предметы по заданному тексту и возвращает список их DTO.
    @GetMapping("/search")
    public Collection<ItemDto> findItem(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findItem(text, userId);
    }
}

