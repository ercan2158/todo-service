package app.ercan.todoservice.controller;

import app.ercan.todoservice.dto.AddTodoItemDto;
import app.ercan.todoservice.dto.TodoItemDto;
import app.ercan.todoservice.service.TodoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@Tag(name = "TodoItemController", description = "REST APIs for managing to-do items")
public class TodoItemController {

    private final TodoItemService todoItemService;

    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @PostMapping
    @Operation(summary = "Add a new item")
    public TodoItemDto addItem(@RequestBody AddTodoItemDto addTodoItemDto) {
        return todoItemService.addItem(addTodoItemDto);
    }

    @GetMapping
    @Operation(summary = "Get all items or only not done items")
    public List<TodoItemDto> getAllItems(@RequestParam(value = "all", defaultValue = "false") boolean all) {
        return all ? todoItemService.getAllItems() : todoItemService.getAllNotDoneItems();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a specific item")
    public TodoItemDto getItemDetails(@PathVariable Long id) {
        return todoItemService.getItemDetails(id);
    }

    @PutMapping("/{id}/done")
    @Operation(summary = "Mark an item as done")
    public TodoItemDto markAsDone(@PathVariable Long id) {
        return todoItemService.markAsDone(id);
    }

    @PutMapping("/{id}/not-done")
    @Operation(summary = "Mark an item as not done")
    public TodoItemDto markAsNotDone(@PathVariable Long id) {
        return todoItemService.markAsNotDone(id);
    }


}
