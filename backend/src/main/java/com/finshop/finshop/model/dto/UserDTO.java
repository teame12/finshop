package com.finshop.finshop.model.dto;

import com.finshop.finshop.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
}
