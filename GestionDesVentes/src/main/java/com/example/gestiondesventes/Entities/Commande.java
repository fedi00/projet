package com.example.gestiondesventes.Entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "t_Commande")
public class Commande {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Double total;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Produit> produits;
	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false) // Associe la clé étrangère
	private Client client;
	@Column(name = "date_commande", updatable = true, nullable = false)
	private LocalDateTime dateCommande;

	@PrePersist
	protected void onCreate() {
		this.dateCommande = LocalDateTime.now();
	}

	// Getters and Setters
}
