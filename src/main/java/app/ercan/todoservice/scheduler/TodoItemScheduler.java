package app.ercan.todoservice.scheduler;

import app.ercan.todoservice.service.TodoItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TodoItemScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TodoItemScheduler.class);


    private final TodoItemService todoItemService;

    public TodoItemScheduler(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @Scheduled(fixedRateString = "${todo-service.check-past-due-interval}")
    public void updatePastDueItems() {

        logger.info("Starting scheduled task to update past due items...");

        long startTime = System.currentTimeMillis();

        todoItemService.updatePastDueItems();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info("Scheduled task completed. Time taken: {} ms", duration);

    }
}