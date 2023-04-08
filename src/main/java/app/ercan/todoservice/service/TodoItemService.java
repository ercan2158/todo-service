package app.ercan.todoservice.service;

import app.ercan.todoservice.dto.AddTodoItemDto;
import app.ercan.todoservice.dto.TodoItemDto;
import app.ercan.todoservice.entity.Status;
import app.ercan.todoservice.entity.TodoItem;
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

    public List<TodoItemDto> getAllItems(boolean all) {

        List<TodoItem> todoItems;

        if (all) {
            todoItems = todoItemRepository.findAll();
        } else {
            todoItems = todoItemRepository.findByStatus(Status.NOT_DONE);
        }

        return todoItems.stream()
                .map(this::toTodoItemDTO)
                .collect(Collectors.toList());
    }


    public TodoItemDto getItemDetails(Long id) {
        return toTodoItemDTO(findTodoItemById(id));
    }

    private TodoItem findTodoItemById(Long id) {
        return todoItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));
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
