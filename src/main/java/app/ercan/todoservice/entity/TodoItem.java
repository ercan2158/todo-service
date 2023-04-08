package app.ercan.todoservice.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime creationDateTime;

    private LocalDateTime dueDateTime;

    private LocalDateTime completionDateTime;


}

