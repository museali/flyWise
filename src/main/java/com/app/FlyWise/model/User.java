package com.app.FlyWise.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")  // Avoid PostgreSQL keyword conflict
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
