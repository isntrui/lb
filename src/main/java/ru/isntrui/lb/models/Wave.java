package ru.isntrui.lb.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.isntrui.lb.enums.WaveStatus;

import java.sql.Date;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of="id")
public class Wave {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private Date starts_on;
    private Date ends_on;

    @Enumerated(EnumType.STRING)
    private WaveStatus status;

    @ManyToMany
    private Set<Song> songs;
}
