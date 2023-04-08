package app.ercan.todoservice.service;

import app.ercan.todoservice.dto.AddTodoItemDto;
import app.ercan.todoservice.dto.TodoItemDto;
import app.ercan.todoservice.entity.Status;
import app.ercan.todoservice.entity.TodoItem;
import app.ercan.todoservice.exception.ItemPastDueException;
import app.ercan.todoservice.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;

    @Autowired
    public TodoItemService(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    public TodoItemDto addItem(AddTodoItemDto addTodoItemDto) {
        TodoItem todoItem = new TodoItem();
        todoItem.setDescription(addTodoItemDto.getDescription());
        todoItem.setDueDateTime(addTodoItemDto.getDueDateTime());
        todoItem.setCreationDateTime(LocalDateTime.now());
        todoItem.setStatus(Status.NOT_DONE);

        TodoItem savedItem = todoItemRepository.save(todoItem);
        return toTodoItemDTO(savedItem);
    }


    public List<TodoItemDto> getAllItems() {

        updatePastDueItems();// Before returning the items, ensure that all the past due items are updated.

        return todoItemRepository.findAll().stream()
                .map(this::toTodoItemDTO)
                .collect(Collectors.toList());
    }

    public List<TodoItemDto> getAllNotDoneItems() {

        return todoItemRepository.findByStatus(Status.NOT_DONE).stream()
                .map(this::toTodoItemDTO)
                .collect(Collectors.toList());
    }


    public TodoItemDto getItemDetails(Long id) {
        //Before returning the item, ensure that the past due is updated.
        todoItemRepository.updatePastDueItem(LocalDateTime.now(), id);

        return toTodoItemDTO(findTodoItemById(id));
    }

    private TodoItem findTodoItemById(Long id) {
        return todoItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));
    }

    public TodoItemDto markAsDone(Long id) {
        TodoItem todoItem = findTodoItemById(id);

        checkIfPastDue(todoItem);

        todoItem.setStatus(Status.DONE);
        todoItem.setCompletionDateTime(LocalDateTime.now());
        TodoItem updatedItem = todoItemRepository.save(todoItem);
        return toTodoItemDTO(updatedItem);
    }

    public TodoItemDto markAsNotDone(Long id) {
        TodoItem todoItem = findTodoItemById(id);

        checkIfPastDue(todoItem);

        todoItem.setStatus(Status.NOT_DONE);
        todoItem.setCompletionDateTime(null);
        TodoItem updatedItem = todoItemRepository.save(todoItem);
        return toTodoItemDTO(updatedItem);
    }

    public void updatePastDueItems() {
        LocalDateTime now = LocalDateTime.now();
        todoItemRepository.updatePastDueItems(now);
    }

    private void checkIfPastDue(TodoItem todoItem) {
        if (todoItem.getStatus() == Status.PAST_DUE) {
            throw new ItemPastDueException("Item with id: " + todoItem.getId() + " is past due and cannot be modified.");
        }
    }

    TodoItemDto toTodoItemDTO(TodoItem todoItem) {
        if (todoItem == null) {
            return null;
        }

        return TodoItemDto.builder()
                .id(todoItem.getId())
                .description(todoItem.getDescription())
                .status(todoItem.getStatus())
                .creationDateTime(todoItem.getCreationDateTime())
                .dueDateTime(todoItem.getDueDateTime())
                .completionDateTime(todoItem.getCompletionDateTime())
                .build();
    }

}
