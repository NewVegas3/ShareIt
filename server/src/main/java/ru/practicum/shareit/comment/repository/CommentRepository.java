package ru.practicum.shareit.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.model.Comment;

import java.util.Set;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findAllByItemId(long itemId);
}
