package ru.isntrui.lb.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import ru.isntrui.lb.enums.Role;

import java.sql.Date;

@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "id")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Getter(AccessLevel.PUBLIC)
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

    @Column(unique = true)
    @Nullable
    private String tgUsername;

    @Nullable
    private String avatarUrl;
}