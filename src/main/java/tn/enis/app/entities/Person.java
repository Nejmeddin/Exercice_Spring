package tn.enis.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "persons")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nom obligatoire")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Prénom obligatoire")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Civility civilite;

    @Size(max = 255)
    @Column(length = 255)
    private String adresse;

    @PastOrPresent(message = "La date de naissance doit être dans le passé ou aujourd'hui")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    public enum Civility {
        M, Mme, Mlle
    }
}