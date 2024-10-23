package ru.isntrui.lb.exceptions.tasks;

public class TaskAlreadyTakenException extends RuntimeException {
    public TaskAlreadyTakenException(Long taskId) {
        super("Task already taken with ID: " + taskId);
    }
}
