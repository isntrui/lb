package ru.isntrui.lb.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.isntrui.lb.models.Task

interface TaskRepository : JpaRepository<Task, Long> {

}