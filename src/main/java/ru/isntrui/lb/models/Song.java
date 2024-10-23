package ru.isntrui.lb.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

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
    private User madeBy;

    private Date createdOn;

    private boolean isApproved = false;

    @ManyToOne
    private Wave wave;

    @ManyToOne
    private User approvedBy;
    private Date approvedOn;
}
