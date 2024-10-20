package ru.isntrui.lb.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of="id")
@Table(name="songs")
public class Song {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String artist;
    private String description;

    @Column(unique = true, nullable = false)
    private String url;

    @ManyToOne
    private User made_by;

    private Date created_on;

    private boolean is_approved;

    @ManyToOne
    private Wave wave;

    @ManyToOne
    private User approved_by;
    private Date approved_on;
}
