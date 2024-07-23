package com.example.gestiondesventes.Repositories;

import com.example.gestiondesventes.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends  JpaRepository <Client, Long> {

}
