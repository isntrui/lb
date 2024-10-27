package ru.isntrui.lb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.exceptions.UnauthorizedException;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.repositories.UserRepository;

import java.util.EnumSet;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private InviteService inviteService;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    @Transactional
    public void changeRole(String email, Role role) throws UserNotFoundException{
        validateRole(role);
        User user = getUserByEmail(email);
        userRepository.updateRole(user.getId(), role);
    }

    private void validateRole(Role role) throws IllegalArgumentException {
        if (role == null || !EnumSet.allOf(Role.class).contains(role)) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) throws UnauthorizedException, UserNotFoundException {
        User user = getUserById(id);
        validatePassword(user, oldPassword);
        userRepository.updatePassword(user.getId(), newPassword);
    }

    @Transactional
    public void changePassword(String email, String newPassword) {
        User user = getUserByEmail(email);
        userRepository.updatePassword(user.getId(), newPassword);
    }

    @Transactional
    public void remove(String email) {
        User user = getUserByEmail(email);
        userRepository.delete(user);
    }

    @Transactional
    public void remove(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }


    public User getUserByEmail(String email) throws UserNotFoundException{
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public User getUserById(Long id) throws UserNotFoundException{
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void validatePassword(User user, String oldPassword) {
        if (!oldPassword.equals(user.getPassword())) {
            throw new UnauthorizedException();
        }
    }

    public Optional<Boolean> isGraduated(Long user) {
        try {
            return Optional.of(getUserById(user).getGraduateYear() < 2022);
        } catch (UserNotFoundException e) {
            return Optional.empty();
        }
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }


    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

    }
}