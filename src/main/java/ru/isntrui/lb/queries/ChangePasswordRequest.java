package ru.isntrui.lb.queries;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Schema(description = "Request to change password")
public record ChangePasswordRequest(String email, String password, @Nullable String oldPassword) {
}
