package ru.isntrui.lb.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "design")
public class Design {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User createdBy;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne
    private Wave wave;

    private boolean isApproved;

    @ManyToOne
    private User approvedBy;

    @Nullable
    private LocalDateTime approvedOn;

    private String url;

    private String title;
}
