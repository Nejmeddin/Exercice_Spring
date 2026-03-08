package tn.enis.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Login obligatoire")
    @Size(max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String login;

    @NotBlank(message = "Mot de passe obligatoire")
    @Size(min = 4, max = 255)
    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    public enum Role {
        USER, ADMIN
    }
}