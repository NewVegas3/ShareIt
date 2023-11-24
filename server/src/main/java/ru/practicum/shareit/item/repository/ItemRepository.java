package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(long userId);

    List<Item> findByUserIdOrderById(long userId);

    @Query(value = "select * " +
            "from items " +
            "where is_available = true " +
            "and (name ilike %:text% " +
            "or description ilike %:text%)",
            nativeQuery = true)
    List<Item> findByNameOrDescriptionContainingIgnoreCase(String text);

    List<Item> findByRequestId(long requestId);
}
