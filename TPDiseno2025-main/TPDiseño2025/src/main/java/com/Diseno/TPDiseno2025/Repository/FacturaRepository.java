package com.Diseno.TPDiseno2025.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Factura;

@Repository
public interface FacturaRepository
        extends JpaRepository<Factura, Integer> {

    boolean existsByIdEstadia(Estadia estadia);
}
