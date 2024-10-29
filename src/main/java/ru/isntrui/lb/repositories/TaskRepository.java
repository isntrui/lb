package ru.isntrui.lb.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isntrui.lb.models.Task;
import ru.isntrui.lb.models.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByTakenById(Long id);
    List<Task> findAllByCreatedById(Long id);
    List<Task> findTasksByTakenBy(User user);
}