package ru.isntrui.lb.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.models.Task;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findAllByTakenById(user.getId());
    }

    public List<Task> getTasksTakenBy(User user) {
        return taskRepository.findTasksByTakenBy(user);
    }

    public List<Task> getTasksCreatedByUser(User user) {
        return taskRepository.findAllByCreatedById(user.getId());
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }


    @Transactional
    public void updateTask(Task task) {
        taskRepository.save(task);
    }
}