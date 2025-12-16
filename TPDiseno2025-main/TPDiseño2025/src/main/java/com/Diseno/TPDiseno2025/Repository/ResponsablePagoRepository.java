package com.Diseno.TPDiseno2025.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.ResponsablePago;

@Repository
public interface ResponsablePagoRepository
        extends JpaRepository<ResponsablePago, Integer> {

    Optional<ResponsablePago> findByCuit(String cuit);
    Optional<ResponsablePago> findByDni(String dni);
}
