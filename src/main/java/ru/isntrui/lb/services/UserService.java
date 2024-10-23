package ru.isntrui.lb.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.enums.UserStatus;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        return userRepository.save(user);
    }

    public UserStatus changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return UserStatus.NOTFOUND;
        }
        User user = userOptional.get();
        if (!oldPassword.equals(user.getPassword())) {
            return UserStatus.UNAUTHORIZED;
        }

        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        if (!newPassword.matches(regex)) {
            return UserStatus.BADREQUEST;
        }

        userRepository.updatePassword(user.getId(), newPassword);
        return UserStatus.OK;
    }

    public UserStatus changePassword(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return UserStatus.NOTFOUND;
        }
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        if (!newPassword.matches(regex)) {
            return UserStatus.BADREQUEST;
        }
        userRepository.updatePassword(userOptional.get().getId(), newPassword);
        return UserStatus.OK;
    }

    public UserStatus remove(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return UserStatus.NOTFOUND;
        }
        userRepository.delete(userOptional.get());
        return UserStatus.OK;
    }

    public UserStatus remove(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return UserStatus.NOTFOUND;
        }
        userRepository.delete(userOptional.get());
        return UserStatus.OK;
    }

    public UserStatus changeRole(String email, Role role) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return UserStatus.NOTFOUND;
        }
        userRepository.updateRole(userOptional.get().getId(), role);
        return UserStatus.OK;
    }
}