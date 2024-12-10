package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

    public User(Long id, String email, String password, UserRole role, String name, String phoneNumber, Cart cart) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.cart = cart;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonIgnore
    private UserRole role;

    @Column(nullable = false)
    @JsonIgnore
    private String name;

    @Column(name = "phone_number", nullable = false)
    @JsonIgnore
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String username;

    @OneToOne(mappedBy = "user")
    private Cart cart;
}

