package com.finshop.finshop.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Email invalide")
    @NotBlank(message = "l'email est obligatoire")
    private String email;

    @NotBlank(message = "le mot de passe est obligatoire")
    private String password;
}
