package ru.isntrui.lb.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.isntrui.lb.enums.Role;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "id")
@Data
public class User implements UserDetails {
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

    @Min(2022)
    @Max(2028)
    private int graduateYear;
    private String building;

    @CreationTimestamp
    private LocalDateTime registered_on;
    private String tgUsername;
    @Column(unique = true)
    private String username;

    @Nullable
    private String avatarUrl;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        // write a builder
        User user = new User();
        public UserBuilder username(String usernameB) {
            user.setUsername(usernameB);
            return this;
        }
        public UserBuilder email(String emailB) {
            user.setEmail(emailB);
            return this;
        }
        public UserBuilder password(String passwordB) {
            user.setPassword(passwordB);
            return this;
        }
        public UserBuilder role(Role roleB) {
            user.setRole(roleB);
            return this;
        }

        public UserBuilder firstName(String firstNameB) {
            user.setFirstName(firstNameB);
            return this;
        }

        public UserBuilder lastName(String lastNameB) {
            user.setLastName(lastNameB);
            return this;
        }

        public UserBuilder graduateYear(int graduateYearB) {
            user.setGraduateYear(graduateYearB);
            return this;
        }

        public UserBuilder building(String buildingB) {
            user.setBuilding(buildingB);
            return this;
        }

        public UserBuilder avatarUrl(String avatarUrlB) {
            user.setAvatarUrl(avatarUrlB);
            return this;
        }

        public User build() {
            return user;
        }
    }
}