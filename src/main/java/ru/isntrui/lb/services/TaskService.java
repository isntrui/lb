package ru.isntrui.lb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.enums.TaskStatus;
import ru.isntrui.lb.models.Task;
import ru.isntrui.lb.repositories.TaskRepository;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public void changeTaskStatus(Long taskId, TaskStatus newStatus) {
        int updatedRows = taskRepository.updateTaskStatus(taskId, newStatus);
        if (updatedRows == 0) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
    }
}