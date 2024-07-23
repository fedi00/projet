package com.example.gestiondesventes.Repositories;

import com.example.gestiondesventes.Entities.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PanierRepository extends JpaRepository <Panier, Long>{

}


