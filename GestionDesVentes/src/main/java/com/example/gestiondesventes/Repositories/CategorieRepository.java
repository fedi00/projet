package com.example.gestiondesventes.Repositories;

import com.example.gestiondesventes.Entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategorieRepository extends JpaRepository <Categorie, Long>{

}



