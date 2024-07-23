package com.example.gestiondesventes.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "t_Produit")
public class Produit {

	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	private long id ;
	
	@NotBlank(message = "Le nom est obligatoire")
	@Column(name = "nom")
	private String nom;
	
	@Column(name = "description")
	private String description;
	
	@NotNull(message = "Le prix est obligatoire")
	@Column (name="prix")
	private Double prix ;
	
	@NotNull(message = "Le stock est obligatoire")
	@Column(name = "stock")
	private int stock;

	@NotBlank(message = "Le chemin de l'image est obligatoire")
	@Column(name = "image")
	private String image;
	

	
	@ManyToOne
	private Categorie category;
}
