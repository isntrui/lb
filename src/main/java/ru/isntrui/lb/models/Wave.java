package ru.isntrui.lb.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import ru.isntrui.lb.enums.WaveStatus;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of="id")
@Table(name = "waves")
public class Wave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate startsOn;
    private LocalDate endsOn;

    @Enumerated(EnumType.STRING)
    private WaveStatus status;

    @ManyToMany
    private Set<Song> songs;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;
    public Wave() {
        title = "null";
        startsOn = Date.valueOf(LocalDate.of(2024, 1, 1)).toLocalDate();
        endsOn = Date.valueOf(LocalDate.of(2024, 1, 2)).toLocalDate();
        status = WaveStatus.PLANNED;
    }
}
