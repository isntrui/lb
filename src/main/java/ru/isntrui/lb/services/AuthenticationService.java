package ru.isntrui.lb.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.exceptions.user.UserNotFoundException;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.queries.ChangePasswordRequest;
import ru.isntrui.lb.queries.JwtAuthenticationResponse;
import ru.isntrui.lb.queries.SignInRequest;
import ru.isntrui.lb.queries.SignUpRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final InviteService inviteService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(@NotNull SignUpRequest request) {
        if (inviteService.findByCode(request.getInviteCode()) == null) {
            throw new RuntimeException("Приглашение не найдено");
        }

        if (!inviteService.findByCode(request.getInviteCode()).getEmail().equals(request.getEmail())) {
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
        userService.create(user);
        inviteService.use(request.getInviteCode(), user.getId());
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(@NotNull SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public void changePassword(@NotNull ChangePasswordRequest cp) throws UserNotFoundException{
        User user = userService.getUserByEmail(cp.email());
        if (!passwordEncoder.matches(cp.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }
        user.setPassword(passwordEncoder.encode(cp.password()));
        userService.changePassword(user.getEmail(), user.getPassword());
    }
}
