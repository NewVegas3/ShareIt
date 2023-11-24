package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.comment.dto.CommentDtoFull;
import ru.practicum.shareit.item.dto.ItemDtoFull;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoFullTest {
    @Autowired
    private JacksonTester<ItemDtoFull> json;

    @Test
    void testItemDtoOwner() throws Exception {
        BookingDtoForItem lastBooking = new BookingDtoForItem();
        lastBooking.setId(3L);
        lastBooking.setBookerId(2L);
        BookingDtoForItem nextBooking = new BookingDtoForItem();
        nextBooking.setId(5L);
        nextBooking.setBookerId(2L);
        CommentDtoFull commentDto1 = new CommentDtoFull();
        commentDto1.setId(6L);
        CommentDtoFull commentDto2 = new CommentDtoFull();
        commentDto2.setId(8L);
        List<CommentDtoFull> comments = List.of(commentDto1, commentDto2);

        ItemDtoFull itemDto = new ItemDtoFull();
        itemDto.setId(1L);
        itemDto.setName("item 1");
        itemDto.setDescription("description 1");
        itemDto.setAvailable(true);
        itemDto.setUserId(1L);
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        itemDto.setComments(List.of(commentDto1, commentDto2));

        JsonContent<ItemDtoFull> result = json.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(itemDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDto.getAvailable().booleanValue());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(Math.toIntExact(itemDto.getLastBooking().getId()));
        assertThat(result).extractingJsonPathNumberValue("$.userId")
                .isEqualTo(Math.toIntExact(itemDto.getUserId()));
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId")
                .isEqualTo(Math.toIntExact(itemDto.getLastBooking().getBookerId()));
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id")
                .isEqualTo(Math.toIntExact(itemDto.getNextBooking().getId()));
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId")
                .isEqualTo(Math.toIntExact(itemDto.getNextBooking().getBookerId()));
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id")
                .isEqualTo(itemDto.getComments().get(0).getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.comments[1].id")
                .isEqualTo(itemDto.getComments().get(1).getId().intValue());
    }
}
