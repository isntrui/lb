package ru.isntrui.lb.queries;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

@Schema(description = "Request to change password")
public record ChangePasswordRequest(String email, String password, @Nullable String oldPassword) {
}
