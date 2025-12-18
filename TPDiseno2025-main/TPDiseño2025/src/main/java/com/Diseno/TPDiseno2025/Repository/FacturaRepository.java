package com.Diseno.TPDiseno2025.Repository;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Diseno.TPDiseno2025.Domain.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    Integer countByFecha(LocalDate fecha);
}
