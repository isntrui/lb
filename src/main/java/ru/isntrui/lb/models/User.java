package ru.isntrui.lb.models;

import jakarta.persistence.*;
import lombok.*;
import ru.isntrui.lb.states.Role;

import java.sql.Date;

@Entity
@Data
@Table(name = "users")
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String firstName;
    private String lastName;
    private int graduateYear;
    private String building;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private Date registered_on;
}
