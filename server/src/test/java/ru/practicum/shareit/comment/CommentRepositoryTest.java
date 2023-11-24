package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    private User user = new User(1L, "test","test@mail.ru");

    private final Item item = new Item(1L, "test", "test", true, user, new ItemRequest(1L, "test", LocalDateTime.now(), user));

    private Comment comment;

    @BeforeEach
    public void start() {
        comment = new Comment();
        comment.setText("test");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
    }

    @Test
    public void findAllWithEmptyRepository_shouldReturnEmpty() {
        List<Comment> comments = commentRepository.findAll();

        Assertions.assertEquals(0, comments.size());
    }
}
