package com.Diseno.TPDiseno2025.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Diseno.TPDiseno2025.Domain.ResponsablePago;

@Repository
public interface ResponsablePagoRepository extends JpaRepository<ResponsablePago, Integer> {

}
