package ru.isntrui.lb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.enums.TaskStatus;
import ru.isntrui.lb.models.Task;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.repositories.TaskRepository;

import java.util.List;
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

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Optional<Task> create(Task task) {
        return Optional.of(taskRepository.save(task));
    }

    public void delete(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public void takeTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        if (task.getTakenBy() != null) {
            throw new RuntimeException("Task already taken");
        }
        task.setTakenBy(user);
        taskRepository.save(task);
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByTakenById(user.getId());
    }

    public List<Task> getTasksCreatedByUser(User user) {
        return taskRepository.findByCreatedById(user.getId());
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        task.setCompleted(true);
        task.setTaskStatus(TaskStatus.DONE);
        taskRepository.save(task);
    }

    public void uncompleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        task.setCompleted(false);
        task.setTaskStatus(TaskStatus.PROGRESS);
        taskRepository.save(task);
    }

    public void updateTask(Task task) {
        taskRepository.save(task);
    }
}