package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Set<Item> findByUserId(long userId);

    @Query(value = "select * " +
            "from items " +
            "where is_available = true " +
            "and (name ilike %:text% " +
            "or description ilike %:text%)",
            nativeQuery = true)
    Set<Item> findByNameOrDescriptionContainingIgnoreCase(String text);
}
