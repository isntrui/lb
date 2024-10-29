package ru.isntrui.lb.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.enums.TaskStatus;
import ru.isntrui.lb.exceptions.tasks.TaskAlreadyTakenException;
import ru.isntrui.lb.exceptions.tasks.TaskNotFoundException;
import ru.isntrui.lb.models.Task;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.repositories.TaskRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void changeTaskStatus(Long taskId, TaskStatus newStatus) {
        int updatedRows = taskRepository.updateTaskStatus(taskId, newStatus);
        if (updatedRows == 0) {
            throw new TaskNotFoundException(taskId);
        }
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public void takeTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        if (task.getTakenBy() != null) {
            throw new TaskAlreadyTakenException(taskId);
        }
        task.setTakenBy(user);
        taskRepository.save(task);
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findAllByTakenById(user.getId());
    }

    public List<Task> getTasksCreatedByUser(User user) {
        return taskRepository.findAllByCreatedById(user.getId());
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public void completeTask(Long taskId, boolean isCompleted) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        task.setCompleted(isCompleted);
        task.setTaskStatus(isCompleted ? TaskStatus.DONE : TaskStatus.PROGRESS);
        if (!isCompleted) task.setMadeOn(new Date(System.currentTimeMillis()));
        taskRepository.save(task);
    }

    @Transactional
    public void updateTask(Task task) {
        taskRepository.save(task);
    }
}