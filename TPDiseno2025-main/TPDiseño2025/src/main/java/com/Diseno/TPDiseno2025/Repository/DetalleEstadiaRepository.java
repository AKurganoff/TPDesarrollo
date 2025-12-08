package com.Diseno.TPDiseno2025.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.DetalleEstadia;

@Repository
public interface DetalleEstadiaRepository extends JpaRepository<DetalleEstadia, Integer>{

}
