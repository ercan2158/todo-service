package app.ercan.todoservice.service;

import app.ercan.todoservice.dto.AddTodoItemDto;
import app.ercan.todoservice.dto.TodoItemDto;
import app.ercan.todoservice.entity.Status;
import app.ercan.todoservice.entity.TodoItem;
import app.ercan.todoservice.repository.TodoItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoItemServiceTest {

    @Mock
    private TodoItemRepository todoItemRepository;

    @InjectMocks
    private TodoItemService todoItemService;

    @Captor
    private ArgumentCaptor<TodoItem> todoItemCaptor;

    private TodoItem todoItem;

    @BeforeEach
    void setUp() {
        todoItem = new TodoItem();
        todoItem.setId(1L);
        todoItem.setDescription("Test item");
        todoItem.setStatus(Status.NOT_DONE);
        todoItem.setCreationDateTime(LocalDateTime.now());
        todoItem.setDueDateTime(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testAddItem() {
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItem);

        AddTodoItemDto addTodoItemDto = new AddTodoItemDto();
        addTodoItemDto.setDescription("Test item");
        addTodoItemDto.setDueDateTime(LocalDateTime.now().plusDays(1));
        TodoItemDto result = todoItemService.addItem(addTodoItemDto);

        verify(todoItemRepository).save(todoItemCaptor.capture());
        TodoItem savedTodoItem = todoItemCaptor.getValue();


        assertEquals(addTodoItemDto.getDescription(), savedTodoItem.getDescription());
        assertEquals(addTodoItemDto.getDueDateTime(), savedTodoItem.getDueDateTime());
        assertEquals(todoItem.getId(), result.getId());
        assertEquals(todoItem.getDescription(), result.getDescription());
        assertEquals(todoItem.getStatus(), result.getStatus());
    }

    @Test
    void testGetItemDetails() {
        when(todoItemRepository.findById(anyLong())).thenReturn(Optional.of(todoItem));

        TodoItemDto result = todoItemService.getItemDetails(1L);

        verify(todoItemRepository).findById(1L);
        assertEquals(todoItem.getId(), result.getId());
        assertEquals(todoItem.getDescription(), result.getDescription());
        assertEquals(todoItem.getStatus(), result.getStatus());
    }

    @Test
    void testUpdateDescription() {
        when(todoItemRepository.findById(anyLong())).thenReturn(Optional.of(todoItem));
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItem);

        String newDescription = "Updated test item";
        TodoItemDto result = todoItemService.updateDescription(1L, newDescription);

        verify(todoItemRepository).save(todoItemCaptor.capture());

        TodoItem updatedTodoItem = todoItemCaptor.getValue();
        assertEquals(newDescription, updatedTodoItem.getDescription());
        assertEquals(todoItem.getId(), result.getId());

    }

}
