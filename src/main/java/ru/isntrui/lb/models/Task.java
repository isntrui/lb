package ru.isntrui.lb.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import ru.isntrui.lb.enums.TaskStatus;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="tasks")
@EqualsAndHashCode(of = "id")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Task's id", example = "2")
    private Long id;

    @Schema(description = "Task's title", example = "Make design for new post")
    private String title;

    @Schema(description = "Task's description", example = "It should contain a new logo and a new color scheme")
    private String description;

    @Schema(description = "Task's deadline", example = "2021-12-32")
    private Date deadline;

    @Schema(description = "Task's created date", example = "2021-12-31")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne
    @Schema(description = "Task's creator")
    private User createdBy;

    @Schema(description = "Task's status", example = "TODO")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Schema(description = "Task's priority", example = "true", defaultValue = "true")
    private boolean isShown = true;
    @Schema(description = "Task's wave", example = "1")
    @ManyToOne
    private Wave wave;

    @ManyToOne
    @Nullable
    @Schema(description = "User, taken this task")
    private User takenBy;
    @Nullable
    @Schema(description = "Date, when task was taken")
    private Date takenOn;
    @Nullable
    @Schema(description = "Date, when task was completed")
    private Date madeOn;

    @Schema(description = "Task's completion status", example = "false", defaultValue = "false")
    private boolean isCompleted = false;
}
