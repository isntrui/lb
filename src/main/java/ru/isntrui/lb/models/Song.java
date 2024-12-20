package ru.isntrui.lb.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(of="id")
@Table(name="songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String artist;
    private String description;

    @Column(unique = true, nullable = false)
    private String url;

    @ManyToOne
    private User madeBy;

    @CreationTimestamp
    private LocalDateTime createdOn;
    private boolean isApproved = false;

    @ManyToOne
    private Wave wave;

    @ManyToOne
    @Nullable
    private User approvedBy;
    @Nullable
    private LocalDateTime approvedOn;
}
