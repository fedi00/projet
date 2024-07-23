package com.example.gestiondesventes.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "t_Categorie")
public class Categorie {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	@NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit comporter entre 3 et 50 caractères")
	@NotBlank(message = "Le nom est obligatoire")
	@Column (name="Nom")
    private String nom;
	
	@NotBlank(message = "La description est obligatoire")
    @Size(min = 5, max = 500, message = "La description doit comporter entre 5 et 500 caractères")
	@Column (name="Description")
    private String description ;
    
	@JsonIgnore //pour éviter boucle infinie
	@OneToMany(mappedBy = "category")
    private List<Produit> produits;
}
