package com.example.gestiondesventes.Repositories;

import java.util.List;

import com.example.gestiondesventes.Entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommandeRepository extends JpaRepository < Commande, Long>{

}
