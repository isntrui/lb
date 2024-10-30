package ru.isntrui.lb.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.queries.ChangePasswordRequest;
import ru.isntrui.lb.queries.JwtAuthenticationResponse;
import ru.isntrui.lb.queries.SignInRequest;
import ru.isntrui.lb.queries.SignUpRequest;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final InviteService inviteService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Logger LOG = LoggerFactory
            .getLogger(AuthenticationService.class);
    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(@NotNull SignUpRequest request, String ip) {
        if (inviteService.findByCode(request.getInviteCode()) == null) {
            throw new RuntimeException("Приглашение не найдено");
        }

        if (!Objects.equals(Objects.requireNonNull(inviteService.findByCode(request.getInviteCode())).getEmail(), request.getEmail())) {
            throw new RuntimeException("Email не совпадает с приглашением");
        }
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .building(request.getBuilding())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .graduateYear(request.getYear())
                .build();
        try {
            userService.create(user);
        } catch (Exception ex) {
            throw new IllegalArgumentException();
        }
        inviteService.use(request.getInviteCode(), user.getId());
        var jwt = jwtService.generateToken(user);
        LOG.info("User registered:\n{}\nInvite code: {}\nIP: {}", user, request.getInviteCode(), ip);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(@NotNull SignInRequest request, String ip) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        LOG.info("Logged in using password:\n{}\nIP: {}", user, ip);
        return new JwtAuthenticationResponse(jwt);
    }

    public void changePassword(@NotNull ChangePasswordRequest cp, String ip) throws UserNotFoundException {
        User user;
        try {
            user = userService.getUserByEmail(cp.email());
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (!passwordEncoder.matches(cp.oldPassword(), user.getPassword())) {
            System.out.println("Old: " + passwordEncoder.encode(cp.oldPassword()));
            System.out.println("User: " + user.getPassword());
            System.out.println(passwordEncoder.matches(cp.oldPassword(),user.getPassword()));
            throw new RuntimeException("Неверный пароль");
        }
        user.setPassword(passwordEncoder.encode(cp.password()));
        userService.changePassword(user.getEmail(), user.getPassword());
        LOG.info("Password changed. User:\n{}\nIP: {}", user, ip);
    }
}
