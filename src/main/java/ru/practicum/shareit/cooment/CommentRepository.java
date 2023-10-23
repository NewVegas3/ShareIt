package ru.practicum.shareit.cooment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.cooment.model.Comment;

import java.util.Set;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findAllByItemId(long itemId);
}
