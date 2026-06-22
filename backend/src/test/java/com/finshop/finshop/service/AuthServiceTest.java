package com.finshop.finshop.service;

import com.finshop.finshop.model.dto.UserDTO;
import com.finshop.finshop.model.entity.Role;
import com.finshop.finshop.model.entity.User;
import com.finshop.finshop.model.request.RegisterRequest;
import com.finshop.finshop.repository.UserRepository;
import com.finshop.finshop.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setNom("Dupont");
        registerRequest.setPrenom("Marie");
        registerRequest.setEmail("marie@test.com");
        registerRequest.setPassword("password123");
        registerRequest.setConfirmPassword("password123");
    }

    @Test
    void register_ShouldReturnUserDTO_WhenValidRequest() {
        // ARRANGE
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(User.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Marie")
                .email("marie@test.com")
                .password("hashedPassword")
                .role(Role.CLIENT)
                .build());

        // ACT
        UserDTO result = authService.register(registerRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals("marie@test.com", result.getEmail());
        assertEquals("Dupont", result.getNom());
        assertEquals(Role.CLIENT, result.getRole());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        // ARRANGE
        when(userRepository.existsByEmail(any())).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(registerRequest));
        assertEquals("Email déjà utilisé", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldThrowException_WhenPasswordsDoNotMatch() {
        // ARRANGE
        registerRequest.setConfirmPassword("differentPassword");
        when(userRepository.existsByEmail(any())).thenReturn(false);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(registerRequest));
        assertEquals("Les mots de passe ne correspondent pas", exception.getMessage());
    }
}
