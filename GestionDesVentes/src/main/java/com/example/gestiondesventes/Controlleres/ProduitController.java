package com.example.gestiondesventes.Controlleres;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.gestiondesventes.Entities.Categorie;
import com.example.gestiondesventes.Entities.Panier;
import com.example.gestiondesventes.Entities.Produit;
import com.example.gestiondesventes.Repositories.CategorieRepository;
import com.example.gestiondesventes.Repositories.PanierRepository;
import com.example.gestiondesventes.Repositories.ProduitRepository;
import com.example.gestiondesventes.services.Produitservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping ({"/produits","/home*"})
public class ProduitController {

	private final PanierRepository panierrepository;
	private final ProduitRepository produitrepository;
	@Autowired
	private CategorieRepository categorieRepository;

	@Autowired
	public ProduitController(PanierRepository panierRepository, ProduitRepository produitRepository) {

		this.panierrepository = panierRepository;
		this.produitrepository = produitRepository;

	}

	@Autowired
	private Produitservice produitservice;

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/list")
	public List<Produit> getAllProduits() {
		return (List<Produit>) produitrepository.findAll();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/add")
	public ResponseEntity<Produit> createProduit(
			@RequestParam("nom") String nom,
			@RequestParam("description") String description,
			@RequestParam("prix") Double prix,
			@RequestParam("stock") int stock,
			@RequestParam("image") MultipartFile imageFile,
			@RequestParam("categorieId") Long categorieId) throws IOException {

		// Créer une nouvelle instance de Produit avec les paramètres reçus
		Produit produit = new Produit();
		produit.setNom(nom);
		produit.setDescription(description);
		produit.setPrix(prix);
		produit.setStock(stock);

		// Associer la catégorie au produit
		Categorie categorie = categorieRepository.findById(categorieId)
				.orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + categorieId));
		produit.setCategory(categorie);

		// Ajouter le produit via le service
		Produit savedProduit = produitservice.ajouterProduit(produit, imageFile);

		// Retourner la réponse
		return ResponseEntity.ok(savedProduit);
	}


	@PostMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestParam Long produitId, @RequestParam Long panierId) {
		Produit produit = produitrepository.findById(produitId)
				.orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + produitId));

		Panier panier = panierrepository.findById(panierId)
				.orElseThrow(() -> new RuntimeException("Panier non trouvé avec l'ID: " + panierId));

		// Ajouter le produit au panier
		panier.getProduit().add(produit);
		panierrepository.save(panier);

		return ResponseEntity.ok("Produit ajouté au panier avec succès.");
	}


	@DeleteMapping("/{produitId}")
	public ResponseEntity<?> deleteProduit(@PathVariable Long produitId) {
		return produitrepository.findById(produitId).map(produit -> {
			produitrepository.delete(produit);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new IllegalArgumentException("ProduitId " +
				produitId + " not found"));
	}


	@GetMapping("/{produitId}")
	public Produit getProduit(@PathVariable Long produitId) {

		Optional<Produit> p = produitrepository.findById(produitId);

		return p.get();

	}


	@GetMapping("/byCategory/{categoryId}")
	public List<Produit> getProduitsByCategoryId(@PathVariable long categoryId) {
		return produitrepository.findByCategory_Id(categoryId);
	}

	@PutMapping("/modif/{produitId}")
	public ResponseEntity<Produit> modifierProduit(
			@PathVariable Long produitId,
			@RequestParam("nom") String nom,
			@RequestParam("description") String description,
			@RequestParam("prix") Double prix,
			@RequestParam("stock") int stock,
			@RequestParam("image") MultipartFile imageFile,
			@RequestParam("categorieId") Long categorieId) {

		return produitrepository.findById(produitId).map(produit -> {
			produit.setNom(nom);
			produit.setDescription(description);
			produit.setPrix(prix);
			produit.setStock(stock);

			// Mettre à jour l'image si un nouveau fichier est fourni
			if (imageFile != null && !imageFile.isEmpty()) {
				try {
					produitservice.modifierProduit(produit, imageFile);
				} catch (IOException e) {
					// Retourner une réponse avec un statut d'erreur
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(produit);
				}
			}

			// Associer la catégorie au produit
			Categorie categorie = categorieRepository.findById(categorieId)
					.orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + categorieId));
			produit.setCategory(categorie);

			// Sauvegarder le produit modifié
			Produit savedProduit = produitservice.modifierProduit(produit);

			// Retourner la réponse
			return ResponseEntity.ok(savedProduit);
		}).orElseThrow(() -> new IllegalArgumentException("ProduitId " + produitId + " not found"));
	}

}
	

	
