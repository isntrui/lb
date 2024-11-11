package ru.isntrui.lb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.repositories.UserRepository;

import java.util.EnumSet;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

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

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
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

    public List<User> getAllByRole(Role role) {
        return userRepository.getAllByRole(role);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) throws UserNotFoundException{
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public User getUserById(Long id) throws UserNotFoundException{
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
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

    public void setAvatar(Long id, String url) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setAvatarUrl(url);
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}