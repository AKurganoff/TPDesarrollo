package com.Diseno.TPDiseno2025.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.DetalleEstadia;
import com.Diseno.TPDiseno2025.Domain.Estadia;

@Repository
public interface DetalleEstadiaRepository extends JpaRepository<DetalleEstadia, Integer>{
    List<DetalleEstadia> findByEstadia(Estadia estadia);
}
