package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoFull;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


public class CommentMapperTest {
    private final User user = new User(1L, "test", "rest@mail.ru");

    private final Item item = new Item(1L, "test", "test", true, user, null);

    private final CommentDto commentDto = new CommentDto(1L, "text", item.getId(), user.getId(), LocalDateTime.now());

    @Test
    void toComment() {
        Comment comment = CommentMapper.toComment(commentDto, item, user);

        Assertions.assertEquals(commentDto.getId(), comment.getId());
        Assertions.assertEquals(commentDto.getText(), comment.getText());
        Assertions.assertEquals(item, comment.getItem());
        Assertions.assertEquals(user, comment.getAuthor());
        Assertions.assertEquals(commentDto.getCreated(), comment.getCreated());
    }

    @Test
    void toCommentDto() {
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        CommentDto commentDto1 = CommentMapper.toCommentDto(comment);

        Assertions.assertEquals(comment.getId(), commentDto1.getId());
        Assertions.assertEquals(comment.getText(), commentDto1.getText());
        Assertions.assertEquals(comment.getItem().getId(), commentDto1.getItem());
        Assertions.assertEquals(comment.getAuthor().getId(), commentDto1.getAuthor());
        Assertions.assertEquals(comment.getCreated(), commentDto1.getCreated());
    }

    @Test
    void toCommentDtoFull() {
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        CommentDtoFull commentDtoFull = CommentMapper.toCommentDtoFull(comment);

        Assertions.assertEquals(comment.getId(), commentDtoFull.getId());
        Assertions.assertEquals(comment.getText(), commentDtoFull.getText());
        Assertions.assertEquals(comment.getAuthor().getName(), commentDtoFull.getAuthorName());
        Assertions.assertEquals(comment.getCreated(), commentDtoFull.getCreated());
    }
}
