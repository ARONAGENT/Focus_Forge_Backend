package com.aronJourney.focus_forge.dto.auth;

import com.aronJourney.focus_forge.annotations.PasswordChecker;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Signup request")
public class SignUpRequestDto {

    @NotNull(message = "name field cannot be null")
    @NotBlank(message = "Name should not be Empty ")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @PasswordChecker
    private String password;
}
