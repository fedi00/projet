package com.example.gestiondesventes.Repositories;

import com.example.gestiondesventes.Entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

}
