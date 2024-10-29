package ru.isntrui.lb.repositories;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isntrui.lb.enums.TaskStatus;
import ru.isntrui.lb.models.Task;
import ru.isntrui.lb.models.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByTakenById(Long id);
    List<Task> findAllByCreatedById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.taskStatus = ?2 WHERE t.id = ?1")
    int updateTaskStatus(Long taskId, TaskStatus taskStatus);

    List<Task> findTasksByTakenBy(User user);
}