package com.example.gestiondesventes.Entities;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_Panier")
public class Panier {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	@NotNull(message = "La quantité est obligatoire")
	@Min(value = 0, message = "La quantité ne peut pas être négative")
	@Column (name="Quantité")
	private int quantité;   
	
	@Pattern(regexp = "^(valide|non valide)$", message = "Le statut doit être 'valide' ou 'non valide'")
	@Column(name = "Statut")
	private String statut;




	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Produit> produit = new ArrayList<>();

}