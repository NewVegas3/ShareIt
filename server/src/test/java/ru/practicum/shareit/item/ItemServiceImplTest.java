package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.Status;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @InjectMocks
    ItemServiceImpl itemService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRequestRepository itemRequestRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    BookingRepository bookingRepository;

    @Test
    public void createItem() {
        User user = new User(1L, "Anastasiya", "ana@mail.ru");
        ItemDto itemDto = new ItemDto(1L, "item", "description", true, user.getId(), 1L);
        Item item = ItemMapper.toItem(itemDto, user, new ItemRequest());

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(new ItemRequest()));
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto savedItemDto = itemService.createItem(itemDto, user.getId());

        assertThat(savedItemDto.getId(), notNullValue());
        assertThat(savedItemDto.getName(), equalTo(itemDto.getName()));
        assertThat(savedItemDto.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(savedItemDto.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(savedItemDto.getUserId(), equalTo(itemDto.getUserId()));

        verify(userRepository).existsById(anyLong());
        verify(userRepository).findById(anyLong());
        verify(itemRequestRepository).findById(anyLong());
        verify(itemRepository).save(any());
    }

    @Test
    public void updateItem() {
        User user = new User(1L, "Anastasiya", "ana@mail.ru");
        ItemDto itemDto = new ItemDto(1L, "item", "description", true, user.getId(), 1L);
        Item item = ItemMapper.toItem(itemDto, user, new ItemRequest());
        ItemDto itemDtoForUpdate = new ItemDto(1L, "updatedItem", "updatedDescription", false, user.getId(), 1L);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto updatedItemDto = itemService.updateItem(itemDtoForUpdate, item.getId(), user.getId());

        assertThat(updatedItemDto.getId(), notNullValue());
        assertThat(updatedItemDto.getName(), equalTo(itemDtoForUpdate.getName()));
        assertThat(updatedItemDto.getDescription(), equalTo(itemDtoForUpdate.getDescription()));
        assertThat(updatedItemDto.getAvailable(), equalTo(itemDtoForUpdate.getAvailable()));
        assertThat(updatedItemDto.getUserId(), equalTo(itemDtoForUpdate.getUserId()));

        verify(userRepository).existsById(anyLong());
        verify(itemRepository).existsById(anyLong());
        verify(itemRepository).findById(anyLong());
        verify(itemRepository).save(any());
    }

    @Test
    public void deleteItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Item> item = itemRepository.findById(1L);

        assertThat(item, equalTo(Optional.empty()));

        verify(itemRepository).findById(anyLong());
    }

    @Test
    void getAllItemsByUser_whenInvoked_thenReturnedEmptyList() {
        Long userId = 1L;
        when(userRepository.existsById(anyLong())).thenReturn(true);

        Collection<ItemDtoFull> actualItems = itemService.getAllUsersItems(userId);

        assertThat(actualItems, empty());
        verify(itemRepository, never()).findById(anyLong());
        verify(commentRepository, never()).findAllByItemId(anyLong());
        verify(itemRepository).findByUserId(anyLong());
    }

    @Test
    void getAllItemsByUser_whenInvoked_thenReturnedItemsCollectionInList() {
        User user = new User(1L, "Anastasiya", "ana@mail.ru");
        Item item = new Item(1L, "item", "description", true, user, new ItemRequest());
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, Status.WAITING);

        List<Item> expectedItems = List.of(item);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findByUserId(anyLong())).thenReturn(expectedItems);
        when(bookingRepository.getFirstByItemIdAndEndBeforeOrderByEnd(anyLong(), any())).thenReturn(booking);
        when(bookingRepository.getTopByItemIdAndStartAfterOrderByStart(anyLong(), any())).thenReturn(null);

        Collection<ItemDtoFull> items = itemService.getAllUsersItems(user.getId());
        List<ItemDtoFull> actualItems = new ArrayList<>(items);

        assertThat(expectedItems.size(), equalTo(actualItems.size()));
        assertThat(expectedItems.get(0).getId(), equalTo(actualItems.get(0).getId()));
        assertThat(expectedItems.get(0).getName(), equalTo(actualItems.get(0).getName()));
        assertThat(expectedItems.get(0).getDescription(), equalTo(actualItems.get(0).getDescription()));
        assertThat(expectedItems.get(0).getIsAvailable(), equalTo(actualItems.get(0).getAvailable()));

        verify(itemRepository).findByUserId(anyLong());
        verify(commentRepository).findAllByItemId(anyLong());
    }

    @Test
    void getAllItems_whenInvoked_thenReturnedItemsCollectionInList() {
        User user = new User(1L, "Anastasiya", "ana@mail.ru");
        Item item = new Item(1L, "item", "description", true, user, new ItemRequest());
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, Status.WAITING);

        List<Item> expectedItems = List.of(item);

        when(itemRepository.findAll()).thenReturn(expectedItems);

        Collection<ItemDto> items = itemService.getAllItems();
        List<ItemDto> actualItems = new ArrayList<>(items);

        assertThat(expectedItems.size(), equalTo(actualItems.size()));
        assertThat(expectedItems.get(0).getId(), equalTo(actualItems.get(0).getId()));
        assertThat(expectedItems.get(0).getName(), equalTo(actualItems.get(0).getName()));
        assertThat(expectedItems.get(0).getDescription(), equalTo(actualItems.get(0).getDescription()));
        assertThat(expectedItems.get(0).getIsAvailable(), equalTo(actualItems.get(0).getAvailable()));

        verify(itemRepository).findAll();
    }
}
