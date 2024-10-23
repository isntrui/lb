package ru.isntrui.lb.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import ru.isntrui.lb.enums.Role;

import java.sql.Date;
import java.time.Year;


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

    @Min(2024)
    @Max(2028)
    private int graduateYear;
    private String building;

    private Date registered_on;

    @Column(unique = true)
    @Nullable
    private String tgUsername;

    @Nullable
    private String avatarUrl;
}