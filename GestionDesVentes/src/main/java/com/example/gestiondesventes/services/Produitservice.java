package com.example.gestiondesventes.services;

import com.example.gestiondesventes.Entities.Produit;
import com.example.gestiondesventes.Repositories.ProduitRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class Produitservice {
    @Autowired
    ProduitRepository produitRepository;
    public Produit ajouterProduit(Produit produit, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String imagePath = "C:\\Users\\Admin\\Desktop\\oumaima\\GestionDesVentes\\images\\" + fileName;
            File file = new File(imagePath);
            FileUtils.writeByteArrayToFile(file, imageFile.getBytes());

            // Log to verify the image path
            System.out.println("Image saved at: " + imagePath);

            produit.setImage(fileName);
        }

        produitRepository.save(produit);
        return produit;
    }
    public Produit modifierProduit(Produit produit) {
        // Logique pour modifier un produit sans image
        return produitRepository.save(produit);
    }
    public Produit modifierProduit(Produit produit, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String imagePath = "C:\\Users\\Admin\\Desktop\\oumaima\\GestionDesVentes\\images\\" + fileName;
            File file = new File(imagePath);
            FileUtils.writeByteArrayToFile(file, imageFile.getBytes());

            // Log to verify the image path
            System.out.println("Image saved at: " + imagePath);

            produit.setImage(fileName);
        }

        produitRepository.save(produit);
        return produit;
    }
}
