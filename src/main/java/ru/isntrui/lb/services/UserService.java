package ru.isntrui.lb.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public User createProduct(User user) {
        return userRepository.save(user);
    }

    public UserStatus changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return UserStatus.NOTFOUND;
        }
        User user = userOptional.get();
        if (!oldPassword.equals(user.getPassword())) {
            return UserStatus.UNAUTHORIZED;
        }

        // Regex to validate the new password
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        if (!newPassword.matches(regex)) {
            return UserStatus.BADREQUEST;
        }

        userRepository.updatePassword(user.getId(), newPassword);
        return UserStatus.OK;
    }

    public void changePassword(String email, String newPassword) {
        // This method is not implemented yet
    }
}