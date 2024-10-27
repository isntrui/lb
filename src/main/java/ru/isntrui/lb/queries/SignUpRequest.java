package ru.isntrui.lb.queries;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.isntrui.lb.enums.Role;

@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Роль пользователя", example = "TECHNICAL")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Роль не может быть пустой")
    private Role role;

    @Schema(description = "Имя", example = "Артём")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов")
    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;

    @Schema(description = "Здание", example = "Солянка")
    @Size(min = 2, max = 50, message = "Здание должно состоять от 2 до 50 символов")
    @NotBlank(message = "Здание не может быть пустым")
    private String building;

    @Schema(description = "Год выпуска", example = "2026")
    @NotBlank(message = "Год выпуска не может быть пустым")
    @Min(2025)
    @Max(2035)
    private int year;


    @Schema(description = "Код приглашения", example = "invite_code")
    @NotBlank(message = "Код приглашения не может быть пустыми")
    private String inviteCode;

    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}
