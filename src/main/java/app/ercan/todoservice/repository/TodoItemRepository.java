package app.ercan.todoservice.repository;

import app.ercan.todoservice.entity.Status;
import app.ercan.todoservice.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    List<TodoItem> findByStatus(Status status);

    @Transactional
    @Modifying
    @Query("UPDATE TodoItem t SET t.status = 'PAST_DUE' WHERE t.status = 'NOT_DONE' AND t.dueDateTime < :now")
    void updatePastDueItems(@Param("now") LocalDateTime now);

    @Transactional
    @Modifying
    @Query("UPDATE TodoItem t SET t.status = 'PAST_DUE' WHERE t.status = 'NOT_DONE' AND t.dueDateTime < :now AND t.id = :id")
    void updatePastDueItem(@Param("now") LocalDateTime now, Long id);
}
