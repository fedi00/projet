package com.example.gestiondesventes.Repositories;

import java.util.List;

import com.example.gestiondesventes.Entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;





@Repository
public interface ProduitRepository extends JpaRepository <Produit, Long> {

	
	 List<Produit> findByCategory_Id(Long categoryId);
}


