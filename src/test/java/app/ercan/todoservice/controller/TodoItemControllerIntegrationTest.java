package app.ercan.todoservice.controller;

import app.ercan.todoservice.dto.AddTodoItemDto;
import app.ercan.todoservice.dto.TodoItemDto;
import app.ercan.todoservice.repository.TodoItemRepository;
import app.ercan.todoservice.service.TodoItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private TodoItemService todoItemService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    public void tearDown() {
        todoItemRepository.deleteAll();
    }

    @Test
    void testAddItem() throws Exception {
        AddTodoItemDto addTodoItemDto = new AddTodoItemDto("Sample task", LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTodoItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Sample task"))
                .andExpect(jsonPath("$.status").value("NOT_DONE"))
                .andExpect(jsonPath("$.dueDateTime").isNotEmpty());
    }

    @Test
    void testAddItem_descriptionTooShort() throws Exception {
        AddTodoItemDto addTodoItemDto = new AddTodoItemDto("A", LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTodoItemDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddItem_descriptionTooLong() throws Exception {
        String longDescription = "A".repeat(301);
        AddTodoItemDto addTodoItemDto = new AddTodoItemDto(longDescription, LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTodoItemDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testAddItem_dueDateTimeInPast() throws Exception {
        AddTodoItemDto addTodoItemDto = new AddTodoItemDto("Sample task",
                LocalDateTime.now().minusDays(1)); //due date already pasted

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTodoItemDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testGetAllItems() throws Exception {
        // Add two items
        AddTodoItemDto item1 = new AddTodoItemDto("Item 1", LocalDateTime.now().plusDays(1));
        AddTodoItemDto item2 = new AddTodoItemDto("Item 2", LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(item1)));
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(item2)));

        // Request all items
        mockMvc.perform(get("/todos?all=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Item 1"))
                .andExpect(jsonPath("$[1].description").value("Item 2"));
    }

    @Test
    void testGetItemDetails() throws Exception {
        // Add a todo item
        AddTodoItemDto item = new AddTodoItemDto("Item 1", LocalDateTime.now().plusDays(1));
        MvcResult result = mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andReturn();
        TodoItemDto createdItem = objectMapper.readValue(result.getResponse().getContentAsString(), TodoItemDto.class);

        // Request item details
        mockMvc.perform(get("/todos/" + createdItem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Item 1"));
    }

    @Test
    void testMarkAsDone() throws Exception {
        // Add an item
        AddTodoItemDto item = new AddTodoItemDto("Item 1", LocalDateTime.now().plusDays(1));
        MvcResult result = mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andReturn();
        TodoItemDto createdItem = objectMapper.readValue(result.getResponse().getContentAsString(), TodoItemDto.class);

        // Mark the item as done
        mockMvc.perform(put("/todos/" + createdItem.getId() + "/done"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void testMarkAsNotDone() throws Exception {
        // Add an item
        AddTodoItemDto item = new AddTodoItemDto("Item 1", LocalDateTime.now().plusDays(1));
        MvcResult result = mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andReturn();
        TodoItemDto createdItem = objectMapper.readValue(result.getResponse().getContentAsString(), TodoItemDto.class);

        // Mark the item as done
        mockMvc.perform(put("/todos/" + createdItem.getId() + "/done"));

        // Mark the item as not done
        mockMvc.perform(put("/todos/" + createdItem.getId() + "/not-done"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NOT_DONE"));
    }

    @Test
    void testUpdateDescription_pastDueItem() throws Exception {

        AddTodoItemDto item = new AddTodoItemDto("Item 1", LocalDateTime.now().plusDays(1));
        MvcResult result = mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andReturn();
        TodoItemDto createdItem = objectMapper.readValue(result.getResponse().getContentAsString(), TodoItemDto.class);

        // Force update the item status to PAST_DUE
        todoItemRepository.updatePastDueItem(LocalDateTime.now().plusDays(1), createdItem.getId());

        // Attempt to update the description of a past due item
        mockMvc.perform(put("/todos/" + createdItem.getId() + "/description")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("New Description")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Item with id: " + createdItem.getId() + " is past due and cannot be modified."));
    }


}