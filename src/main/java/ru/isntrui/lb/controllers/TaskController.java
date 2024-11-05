package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.enums.TaskStatus;
import ru.isntrui.lb.models.Task;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.services.TaskService;
import ru.isntrui.lb.services.UserService;

import java.util.Objects;
import java.util.Optional;

@RestController
@Tag(name = "Task")
@RequestMapping("/api/task/")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to create a task")
    })
    @PostMapping
    public ResponseEntity<Task> createTask(
            @Parameter(description = "New task object") @RequestBody Task task
    ) {
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(403).build();
        }
        if (task.getCreatedBy() == null) {
            task.setCreatedBy(userService.getCurrentUser());
        }
        if (task.getTaskStatus() == null) {
            task.setTaskStatus(TaskStatus.TODO);
        }
        if (task.getTakenBy() != null) {
            User user = userService.getUserById(task.getTakenBy().getId());
            if (user != null) {
                task.setTakenBy(user);
            }
        }
        taskService.create(task);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Get task by id")
    @ApiResponse(responseCode = "200", description = "Task found")
    @GetMapping("{id}")
    public ResponseEntity<Task> getTask(
            @PathVariable @Parameter(description = "Task' id") Long id
    ) {
        Optional<Task> t = taskService.getTaskById(id);
        return t.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Set task status")
    @PutMapping("{id}/setStatus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to change that task's status")
    })
    public ResponseEntity<Task> setStatus(
            @PathVariable @Parameter(description = "Task' id", example = "2") Long id,
            @RequestParam @Parameter(description = "New status", example = "TODO") TaskStatus status) {
        Optional<Task> t = taskService.getTaskById(id);
        if (t.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN) || userService.getCurrentUser().getId().equals(Objects.requireNonNull(t.get().getTakenBy()).getId()))) {
            return ResponseEntity.status(403).build();
        }
        Task task = t.get();
        task.setTaskStatus(status);
        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Set completeness of task")
    @PutMapping("{id}/complete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task completed"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to complete that task")
    })
    public ResponseEntity<Task> completeTask(
            @PathVariable @Parameter(description = "Task' id", example = "2") Long id,
            @RequestParam @Parameter(description = "Is task completed", example = "true") boolean isCompleted) {
        Optional<Task> t = taskService.getTaskById(id);
        if (t.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN) || userService.getCurrentUser().getId().equals(Objects.requireNonNull(t.get().getTakenBy()).getId()))) {
            return ResponseEntity.status(403).build();
        }
        Task task = t.get();
        task.setCompleted(isCompleted);
        task.setTaskStatus(isCompleted ? TaskStatus.DONE : TaskStatus.PROGRESS);
        task.setMadeOn(isCompleted ? null : new java.sql.Date(System.currentTimeMillis()));
        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Take task")
    @PutMapping("{id}/take")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task taken"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to take that task")
    })
    public ResponseEntity<Task> takeTask(
            @PathVariable @Parameter(description = "Task' id", example = "2") Long id
    ) {
        Optional<Task> t = taskService.getTaskById(id);
        if (t.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task task = t.get();
        if (task.getTakenBy() != null) {
            return ResponseEntity.status(403).build();
        }
        task.setTakenBy(userService.getCurrentUser());
        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Get tasks created by user")
    @GetMapping("createdBy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to get tasks created by that user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Iterable<Task>> getTasksCreatedByUser(
            @RequestParam @Parameter(description = "User' id", example = "2") Long id
    ) {
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(403).build();
        }
        if (userService.getUserById(id) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(taskService.getTasksCreatedByUser(userService.getUserById(id)));
    }

    @Operation(summary = "Get tasks taken by user")
    @GetMapping("takenBy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to get tasks taken by that user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Iterable<Task>> getTasksTakenByUser(
            @RequestParam @Parameter(description = "User' id", example = "2") Long id
    ) {
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(taskService.getTasksTakenBy(userService.getUserById(id)));
    }

    @Operation(summary = "Update task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to update that task")
    })
    @PutMapping("{id}/update")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Optional<Task> t = taskService.getTaskById(id);
        if (t.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(403).build();
        }
        task.setId(id);
        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Set visibility of task")
    @PutMapping("{id}/visibility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task visibility updated"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to change that task's visibility")
    })
    public ResponseEntity<Task> setVisibility(
            @PathVariable @Parameter(description = "Task' id", example = "2") Long id,
            @RequestParam @Parameter(description = "New visibility", example = "false") boolean visibility) {
        Optional<Task> t = taskService.getTaskById(id);
        if (t.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(403).build();
        }
        Task task = t.get();
        task.setShown(visibility);
        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Delete task")
    @DeleteMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to delete that task")
    })
    public ResponseEntity<Void> deleteTask(
            @PathVariable @Parameter(description = "Task' id") Long id
    ) {
        Optional<Task> t = taskService.getTaskById(id);
        if (t.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(403).build();
        }
        taskService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "Tasks found")
    @GetMapping("all")
    public ResponseEntity<Iterable<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(summary = "Get all my tasks")
    @ApiResponse(responseCode = "200", description = "Tasks found")
    @GetMapping("my")
    public ResponseEntity<Iterable<Task>> getMyTasks() {
        return ResponseEntity.ok(taskService.getTasksByUser(userService.getCurrentUser()));
    }
}
