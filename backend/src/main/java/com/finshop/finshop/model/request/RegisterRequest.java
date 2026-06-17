package com.finshop.finshop.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Size(min = 8, message = "Le mot de passe doit faire minimum 8 caractères")
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @NotBlank(message = "La confirmation est obligatoire")
    private String confirmPassword;
}
