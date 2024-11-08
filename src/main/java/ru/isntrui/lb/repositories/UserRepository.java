package ru.isntrui.lb.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.models.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :id")
    void updatePassword(Long id, String newPassword);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    void updateRole(Long id, Role role);

    List<User> getAllByRole(Role role);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}