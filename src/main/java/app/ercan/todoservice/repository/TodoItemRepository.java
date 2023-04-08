package app.ercan.todoservice.repository;

import app.ercan.todoservice.entity.Status;
import app.ercan.todoservice.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    List<TodoItem> findByStatus(Status status);

}
