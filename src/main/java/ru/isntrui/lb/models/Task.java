package ru.isntrui.lb.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.isntrui.lb.enums.TaskStatus;

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
    private Date createdOn;

    @ManyToOne
    private User createdBy;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    private boolean isShown = true;
    private long wave;

    @ManyToOne
    @Nullable
    private User takenBy;
    @Nullable
    private Date takenOn;
    @Nullable
    private Date madeOn;
    private boolean isCompleted = false;
}
