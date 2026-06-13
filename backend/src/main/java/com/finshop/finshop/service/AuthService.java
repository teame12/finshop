package com.finshop.finshop.service;

import com.finshop.finshop.model.dto.UserDTO;
import com.finshop.finshop.model.entity.Role;
import com.finshop.finshop.model.entity.User;
import com.finshop.finshop.model.request.RegisterRequest;
import com.finshop.finshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO register(RegisterRequest request) {

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Vérifier que les mots de passe correspondent
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Les mots de passe ne correspondent pas");
        }

        // Créer l'utilisateur
        User user = User.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .build();

        // Sauvegarder en base de données
        User savedUser = userRepository.save(user);

        // Retourner le DTO (jamais l'entité !)
        return UserDTO.builder()
                .id(savedUser.getId())
                .nom(savedUser.getNom())
                .prenom(savedUser.getPrenom())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }
}
