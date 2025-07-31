package com.cinnamonbay.backend.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
