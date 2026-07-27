package com.vml.biblioteca.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(unique = true, length = 20)
    private String membershipId;

    private LocalDate registrationDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @PrePersist
    protected void onCreate() {
        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }
        if (membershipId == null) {
            membershipId = "MEM-" + System.currentTimeMillis();
        }
    }
}
