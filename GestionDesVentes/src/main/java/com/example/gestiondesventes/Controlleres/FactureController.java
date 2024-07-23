package com.example.gestiondesventes.Controlleres;

import java.util.List;
import java.util.Optional;

import com.example.gestiondesventes.Entities.Commande;
import com.example.gestiondesventes.Entities.FacilitePaiement;
import com.example.gestiondesventes.Entities.Facture;
import com.example.gestiondesventes.Entities.TypePaiement;
import com.example.gestiondesventes.Repositories.CommandeRepository;
import com.example.gestiondesventes.Repositories.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facture")
public class FactureController {

	     @Autowired
		 FactureRepository factureRepository ;
	@Autowired
	CommandeRepository commandeRepository;



	@GetMapping("/list")
	    public List<Facture> getAllFactures(){
	        return (List<Facture> )factureRepository.findAll();
	    }


	@PostMapping("/creer")
	public ResponseEntity<String> creerFacture(@RequestParam Long commandeId, @RequestParam TypePaiement typePaiement, @RequestParam FacilitePaiement facilitePaiement) {
		Commande commande = commandeRepository.findById(commandeId).orElse(null);
		if (commande == null) {
			return ResponseEntity.badRequest().body("Commande non trouvée");
		}

		Facture facture = new Facture();
		facture.setCommande(commande);
		facture.setTypePaiement(typePaiement);
		facture.setFacilitePaiement(facilitePaiement);

		factureRepository.save(facture);

		return ResponseEntity.ok("Facture créée avec succès");
	}

	    
	    @DeleteMapping("/{deleteId}")
	    public ResponseEntity<?> deleteFacture(@PathVariable Long deleteId){
	        return factureRepository.findById(deleteId).map(facture -> {
	        	factureRepository.delete(facture);
	            return ResponseEntity.ok().build();
	        }).orElseThrow(() -> new IllegalArgumentException("DeleteId" + "deleteId" + " not found"));
	    }

  
       @GetMapping("/{factureId}")
        public Facture getFacture(@PathVariable Long factureId) {
        Optional<Facture> f = factureRepository.findById(factureId);
        return f.get();

        }
	@PutMapping("/modifier/{factureId}")
	public ResponseEntity<Facture> updateFacture(@PathVariable Long factureId, @RequestBody Facture factureRequest) {
		return factureRepository.findById(factureId).map(facture -> {
			facture.setTypePaiement(factureRequest.getTypePaiement());
			facture.setFacilitePaiement(factureRequest.getFacilitePaiement());

			// Calculer le nouveau total avec intérêt
			facture.mettreAJourTotalAvecInteret();

			Facture updatedFacture = factureRepository.save(facture);
			return ResponseEntity.ok(updatedFacture);
		}).orElseThrow(() -> new IllegalArgumentException("FactureId " + factureId + " not found"));
	}
	    
}