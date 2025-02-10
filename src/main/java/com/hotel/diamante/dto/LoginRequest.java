package com.hotel.diamante.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "o email não pode ser nulo")
    private String email;
    @NotBlank(message = "a senha não pode ser nula")
    private String password;
}
