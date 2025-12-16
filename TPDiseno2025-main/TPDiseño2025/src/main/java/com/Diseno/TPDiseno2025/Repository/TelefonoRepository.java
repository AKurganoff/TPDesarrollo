package com.Diseno.TPDiseno2025.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.Telefono;

@Repository
public interface TelefonoRepository extends JpaRepository<Telefono, Integer> {

    Optional<Telefono> findByTelefono(String telefono);

    void deleteByHuesped_Dni(Integer dni);

    boolean existsByHuesped_Dni(Integer dni);
}
