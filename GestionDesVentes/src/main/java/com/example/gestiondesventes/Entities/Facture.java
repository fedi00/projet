package com.example.gestiondesventes.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_Facture")
public class Facture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "commande_id", nullable = false)
	private Commande commande;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TypePaiement typePaiement;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FacilitePaiement facilitePaiement;

	@Column(nullable = false)
	private Double totalAvecInteret;

	@PrePersist
	public void calculerTotalAvecInteret() {
		double total = commande.getTotal();
		switch (facilitePaiement) {
			case TROIS_MOIS:
				totalAvecInteret = total * 1.05;
				break;
			case SIX_MOIS:
				totalAvecInteret = total * 1.10;
				break;
			case UN_AN:
				totalAvecInteret = total * 1.20;
				break;
			case CONTANT:
			default:
				totalAvecInteret = total;
				break;
		}
	}
	public void mettreAJourTotalAvecInteret() {
		double total = commande.getTotal();
		switch (facilitePaiement) {
			case TROIS_MOIS:
				totalAvecInteret = total * 1.05;
				break;
			case SIX_MOIS:
				totalAvecInteret = total * 1.10;
				break;
			case UN_AN:
				totalAvecInteret = total * 1.20;
				break;
			case CONTANT:
			default:
				totalAvecInteret = total;
				break;
		}
	}
}
