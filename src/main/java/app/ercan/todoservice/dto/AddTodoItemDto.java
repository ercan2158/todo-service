package app.ercan.todoservice.dto;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTodoItemDto {

    @NotBlank
    @Size(min = 2, max = 300, message = "The length of this field must be between 2 and 300 characters.")
    private String description;

    @Future(message = "The due date and time must be in the future.")
    private LocalDateTime dueDateTime;
}
