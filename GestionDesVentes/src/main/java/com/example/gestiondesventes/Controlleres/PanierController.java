package com.example.gestiondesventes.Controlleres;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.gestiondesventes.Entities.Commande;
import com.example.gestiondesventes.Entities.Panier;
import com.example.gestiondesventes.Entities.Produit;
import com.example.gestiondesventes.Repositories.CommandeRepository;
import com.example.gestiondesventes.Repositories.PanierRepository;
import com.example.gestiondesventes.Repositories.ProduitRepository;
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
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/panier")
public class PanierController {

  

	    
	    private final PanierRepository panierRepository;
	    private final ProduitRepository produitrepository;
	    private final CommandeRepository commanderepository;

	    @Autowired
	    public PanierController (PanierRepository panierRepository, ProduitRepository produitRepository, CommandeRepository commandeRepository) {
	    	
	    	this.panierRepository = panierRepository;
	        this.produitrepository = produitRepository;
	        this.commanderepository = commandeRepository;
	    }

	    
	    
	    
	   @GetMapping("/list")
	    public List<Panier> getAllPaniers() {
	        return (List<Panier>)panierRepository.findAll();
	    }



	@PostMapping("/add")
	public ResponseEntity<String> createPanier(@RequestBody Panier panier) {
		List<Produit> produitsAttaches = new ArrayList<>();

		// Vérifiez que tous les produits dans le panier existent et ont suffisamment de stock
		for (Produit produit : panier.getProduit()) {
			Produit p = produitrepository.findById(produit.getId()).orElse(null);
			if (p == null) {
				return ResponseEntity.badRequest().body("Produit non trouvé : " + produit.getId());
			}

			int restQuantite = p.getStock() - panier.getQuantité();
			if (restQuantite < 0) {
				return ResponseEntity.badRequest().body("Stock insuffisant pour le produit : " + produit.getNom());
			}

			p.setStock(restQuantite);
			produitrepository.save(p); // Mettez à jour le stock immédiatement
			produitsAttaches.add(p); // Ajoutez le produit attaché à la liste
		}

		// Remplacez les produits du panier par les produits attachés
		panier.setProduit(produitsAttaches);

		// Enregistrez le panier
		try {
			panierRepository.save(panier);
			return ResponseEntity.ok("Panier créé avec succès");
		} catch (Exception e) {
			e.printStackTrace(); // Ajoutez cette ligne pour voir le stacktrace complet dans les logs
			return ResponseEntity.badRequest().body("Erreur lors de la création du panier : " + e.getMessage());
		}
	}








	@PutMapping("/{panierId}")
	    public Panier updatePanier(@PathVariable Long panierId, @Valid @RequestBody Panier PanierRequest) {
	        return panierRepository.findById(panierId).map(panier -> {
	            // Conserver les produits existants
	            List<Produit> produitsExistants = panier.getProduit();

	            // Mettre à jour les champs du panier
	            panier.setQuantité(PanierRequest.getQuantité());
	            panier.setStatut(PanierRequest.getStatut().trim());

	            // Réassigner les produits existants
	            panier.setProduit(produitsExistants);

	            return panierRepository.save(panier);
	        }).orElseThrow(() -> new IllegalArgumentException("PanierId " + panierId + " not found"));
	    }



	  
	    





 
        @GetMapping("/{panierId}")
         public Panier getPanier(@PathVariable Long panierId) {

         Optional<Panier> p = panierRepository.findById(panierId);

         return p.get();

       }
        
        
        
        @GetMapping("/increaseQuantity/{panierId}")
        public Panier increaseQuantity(@PathVariable Long panierId) {
            Panier p = panierRepository.findById(panierId).orElse(null);

            p.setQuantité(p.getQuantité() + 1);
            p.setId(panierId);
            return panierRepository.save(p);

        }
     
        
        
	    
        @GetMapping("/decreaseQuantity/{panierId}")
         public Panier decreaseQuantity(@PathVariable Long panierId) {
            Panier p = panierRepository.findById(panierId).orElse(null);
            if (p.getQuantité() == 1) {
                return p;
            }
            p.setQuantité(p.getQuantité() - 1);
            p.setId(panierId);
            return panierRepository.save(p);

        }



	@PostMapping("addProduitToPanier/{idPanier}/{idProduit}")
	public ResponseEntity<String> addProduitToPanier(@PathVariable long idPanier, @PathVariable long idProduit) {
		Panier panier = panierRepository.findById(idPanier).orElse(null);
		Produit produit = produitrepository.findById(idProduit).orElse(null);

		// Vérifier si le panier et le produit existent
		if (panier == null || produit == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Panier ou Produit non trouvé");
		}

		try {
			// Associer le produit au panier
			panier.getProduit().add(produit);
			panierRepository.save(panier);
			return ResponseEntity.ok("Produit ajouté au panier");
		} catch (Exception e) {
			// Gérer les autres exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout du produit au panier");
		}
	}

	@DeleteMapping("removeProduitFromPanier/{idPanier}/{idProduit}")
	public ResponseEntity<String> removeProduitFromPanier(@PathVariable long idPanier, @PathVariable long idProduit) {
		Panier panier = panierRepository.findById(idPanier).orElse(null);
		Produit produit = produitrepository.findById(idProduit).orElse(null);

		if (panier == null || produit == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Panier ou Produit non trouvé");
		}

		try {
			panier.getProduit().remove(produit);
			panierRepository.save(panier);
			return ResponseEntity.ok("Produit retiré du panier");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du retrait du produit du panier");
		}
	}
        
        
        
}
