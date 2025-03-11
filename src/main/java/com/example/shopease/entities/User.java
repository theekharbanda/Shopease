package com.example.shopease.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role{
        USER,ADMIN
    }

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)     //If you save a user, Associated cart will also be saved;
    private Cart cart;

    public boolean isAdmin(){
        return role.equals(Role.ADMIN);
    }
}
