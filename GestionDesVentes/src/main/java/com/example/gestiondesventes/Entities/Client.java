package com.example.gestiondesventes.Entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_client")
public class Client {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Le nom est obligatoire")
    @Size(min = 3, max = 20, message = "Le nom doit contenir entre 3 et 20 caractères")
    @Column(name = "nom")
    private String nom;

    @NotEmpty(message = "Le prénom est obligatoire")
    @Size(min = 3, max = 20, message = "Le prénom doit contenir entre 3 et 20 caractères")
    @Column(name = "prenom")
    private String prenom;

    @NotEmpty(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "L'adresse est obligatoire")
    @Column(name = "adresse")
    private String adresse;

    @NotEmpty(message = "Le téléphone est obligatoire")
    @Column(name = "telephone")
    private String telephone;
    @OneToMany
    private List<Commande> commandes;
    
    }
