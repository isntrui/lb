package ru.isntrui.lb.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.isntrui.lb.states.TaskStatus;

import java.sql.Date;

@Entity
@Data
@Table(name="tasks")
@EqualsAndHashCode(of = "id")
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Date deadline;
    private Date created_on;

    @ManyToOne
    private User created_by;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    private boolean isShown = true;
    private long wave;

    @ManyToOne
    @Nullable
    private User taken_by;
    @Nullable
    private Date taken_on;
    @Nullable
    private Date made_at;
    private boolean is_completed = false;
}
