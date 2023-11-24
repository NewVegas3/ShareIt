package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUser(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен список вещей пользователя с id = {}, from = {}, size = {}.",
                userId, from, size);
        return itemClient.getAllItemsByUser(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        log.info("Получена вещь с id = {}, userId={}.", itemId, userId);
        return itemClient.getItemById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemInDto itemInDto) {
        log.info("Добавлена новая вещь: {}, userId={}.", itemInDto, userId);
        return itemClient.saveItem(userId, itemInDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId, @RequestBody ItemInDto itemInDto) {
        log.info("Обновлена вещь с id = {}:  {}, userId={}.", itemId, itemInDto, userId);
        return itemClient.updateItem(userId, itemId, itemInDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItems(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam String text,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен список вещей с текстом: {} пользователя с id = {}, from = {}, size = {}.",
                text, userId, from, size);
        return itemClient.findItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CommentDto commentDto) {
        log.info("Добавлен новый комментарий: {} \n пользователем с id = {} для вещи с id = {}.",
                commentDto, userId, itemId);
        return itemClient.saveComment(userId, itemId, commentDto);
    }

}