package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.WrongAccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemServiceImpl implements ItemService {
    UserService userService;
    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 1;

    public ItemServiceImpl(UserService userService) {
        this.userService = userService;
    }

    // Создает новую вещь и добавляет ее в коллекцию вещей.
    public ItemDto createItem(ItemDto dto, long userId) {
        userService.getUserById(userId); // Проверяет наличие пользователя с заданным id.
        dto.setOwner(userId); // Устанавливает пользователя как владельца вещи.
        dto.setId(nextId); // Устанавливает уникальный id для вещи.
        nextId++;
        Item item = ItemMapper.toItem(dto);
        items.put(item.getId(), item); // Добавляет вещь в коллекцию вещей.
        return ItemMapper.toItemDto(item);
    }

    // Обновляет информацию о вещи (имя, описание, доступность).
    public ItemDto updateItem(ItemDto dto, long itemId, long userId) {
        ItemDto itemUpdate = getItemById(itemId, userId); // Проверяет наличие пользователя с заданным id и вещи в базе.
        if (itemUpdate.getOwner() != 0) {
            accessCheck(itemUpdate.getOwner(), userId); // Проверяет доступ пользователя к вещи.
        }
        if (dto.getName() != null) {
            itemUpdate.setName(dto.getName()); // Обновляет имя вещи.
        }
        if (dto.getDescription() != null) {
            itemUpdate.setDescription(dto.getDescription()); // Обновляет описание вещи.
        }
        if (dto.getAvailable() != null) {
            itemUpdate.setAvailable(dto.getAvailable()); // Обновляет доступность вещи.
        }
        items.put(itemId, ItemMapper.toItem(itemUpdate)); // Обновляет информацию о вещи в коллекции.
        return itemUpdate;
    }

    // Получает информацию о вещи по ее id и возвращает ее в виде DTO объекта.
    public ItemDto getItemById(long id, long userId) {
        // Проверка существования вещи с заданным id
        if (!items.containsKey(id)) {
            // Вещь не найдена, выбрасываем исключение или выполняем нужную обработку ошибки
        }

        // Получение вещи по id
        Item item = items.get(id);

        // Проверка, что пользователь с userId имеет доступ к этой вещи (если требуется)
        // Может потребоваться дополнительная логика для определения доступа

        // Возвращаем вещь в виде DTO
        return ItemMapper.toItemDto(item);
    }

    // Получает список всех вещей пользователя в виде DTO объектов.
    public Collection<ItemDto> getAllUsersItems(long userId) {
        // Создаем список для хранения вещей пользователя
        Collection<ItemDto> userItems = new ArrayList<>();

        // Проходим по всем вещам и добавляем в список только те, которые принадлежат пользователю
        for (Item item : items.values()) {
            if (item.getOwner() == userId) {
                userItems.add(ItemMapper.toItemDto(item));
            }
        }

        return userItems;
    }

    // Ищет вещи по заданному тексту и возвращает их в виде DTO объектов.
    public Collection<ItemDto> findItem(String text, long userId) {
        userService.getUserById(userId);
        if (text.isBlank()) {
            return List.of();
        }
        String textInLowerCase = text.toLowerCase();
        return items.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(textInLowerCase) || item.getDescription().toLowerCase().contains(textInLowerCase))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    // Удаляет вещь по ее id, проверяя доступ пользователя к удалению.
    public void deleteItem(long id, long userId) {
        userService.getUserById(userId); // Проверяет наличие пользователя с заданным id.
        ItemDto dto = getItemById(id, userId); // Проверяет наличие вещи в базе.
        accessCheck(dto.getOwner(), userId); // Проверяет доступ пользователя к вещи.
        items.remove(id); // Удаляет вещь из коллекции.
    }

    // Возвращает список всех вещей в виде DTO объектов.
    public Collection<ItemDto> getAllItems(long userId) {
        userService.getUserById(userId); // Проверяет наличие пользователя с заданным id.
        return items.values()
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    // Проверяет доступ пользователя к редактированию вещи.
    public void accessCheck(long userIdFromItem, long userIdFromRequest) {
        if (userIdFromItem != userIdFromRequest) {
            throw new WrongAccessException("Недостаточно прав для редактирования");
        }
    }
}

