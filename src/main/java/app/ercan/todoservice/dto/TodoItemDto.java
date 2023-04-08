package app.ercan.todoservice.dto;

import app.ercan.todoservice.entity.Status;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TodoItemDto {

    private Long id;

    private String description;

    private Status status;

    private LocalDateTime creationDateTime;

    private LocalDateTime dueDateTime;

    private LocalDateTime completionDateTime;

}

