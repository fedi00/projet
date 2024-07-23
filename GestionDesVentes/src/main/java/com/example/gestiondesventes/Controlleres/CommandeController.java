package com.example.gestiondesventes.Controlleres;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.gestiondesventes.Entities.Client;
import com.example.gestiondesventes.Entities.Commande;
import com.example.gestiondesventes.Entities.Panier;
import com.example.gestiondesventes.Entities.Produit;
import com.example.gestiondesventes.Repositories.ClientRepository;
import com.example.gestiondesventes.Repositories.CommandeRepository;
import com.example.gestiondesventes.Repositories.PanierRepository;
import com.example.gestiondesventes.Repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/commandes")
public class CommandeController {

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CommandeRepository commanderepository;
    @Autowired
    private ClientRepository clientRepository;



    @GetMapping("/list")
    public List<Commande> getAllCommandes() {
        return (List<Commande>)commanderepository.findAll();
    }


    @PostMapping("/convertir/{panierId}/{clientId}")
    public ResponseEntity<String> convertirPanierEnCommande(@PathVariable Long panierId, @PathVariable Long clientId) {
        Panier panier = panierRepository.findById(panierId).orElse(null);
        if (panier == null) {
            return ResponseEntity.badRequest().body("Panier non trouvé");
        }

        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            return ResponseEntity.badRequest().body("Client non trouvé");
        }

        if (!"valide".equals(panier.getStatut())) {
            return ResponseEntity.badRequest().body("Le panier n'est pas valide");
        }

        // Vérifiez et mettez à jour le stock
        for (Produit produit : panier.getProduit()) {
            Produit p = produitRepository.findById(produit.getId()).orElse(null);
            if (p == null) {
                return ResponseEntity.badRequest().body("Produit non trouvé : " + produit.getId());
            }

            int restQuantite = p.getStock() - panier.getQuantité();
            if (restQuantite < 0) {
                return ResponseEntity.badRequest().body("Stock insuffisant pour le produit : " + produit.getNom());
            }

            p.setStock(restQuantite);
            produitRepository.save(p);
        }

        // Créez la commande
        Commande commande = new Commande();
        commande.setClient(client); // Utilise le client récupéré
        List<Produit> produitsCopie = new ArrayList<>(panier.getProduit());
        commande.setProduits(produitsCopie);

        // Calculez le total de la commande
        double total = produitsCopie.stream()
                .mapToDouble(p -> p.getPrix() * panier.getQuantité())
                .sum();
        commande.setTotal(total);

        commandeRepository.save(commande);

        // (Optionnel) Videz ou supprimez le panier
        panierRepository.delete(panier);

        return ResponseEntity.ok("Commande créée avec succès");
    }




    @DeleteMapping("/delete/{commandeId}")
    public ResponseEntity<?> deleteCommande(@PathVariable Long commandeId) {
        return commanderepository.findById(commandeId).map(commande -> {
            commanderepository.delete(commande);
            return ResponseEntity.ok().body("Commande supprimée avec succès");
        }).orElseThrow(() -> new IllegalArgumentException("CommandeId " + commandeId + " not found"));
    }



    @PutMapping("/modifier/{commandeId}")
    public Commande updateCommande(@PathVariable Long commandeId, @Valid @RequestBody Commande CommandeRequest) {
        return commanderepository.findById(commandeId).map(commande -> {
            commande.setTotal(CommandeRequest.getTotal());
            commande.setDateCommande(LocalDateTime.now());
            // Mettez à jour d'autres champs si nécessaire

            return commanderepository.save(commande);
        }).orElseThrow(() -> new IllegalArgumentException("CommandeId " + commandeId + " not found"));
    }



    @GetMapping("/{commandeId}")
    public Commande getCommande(@PathVariable Long commandeId) {
        Optional<Commande> c = commanderepository.findById(commandeId);
        return c.get();

    }
	
	
	
    
  
}