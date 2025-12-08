package com.Diseno.TPDiseno2025.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Diseno.TPDiseno2025.Domain.Juridica;

public interface JuridicaRepository extends JpaRepository<Juridica, Integer>{
    @Override
    Optional<Juridica> findById(Integer cuit);

    boolean existsByCuit(Integer cuit);
    
}
