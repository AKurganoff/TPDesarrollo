package com.Diseno.TPDiseno2025.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    
    @Override
    Optional<Reserva> findById(Integer idReserva);
    
    Boolean existsByHuesped_Dni(Integer dni);

    List<Reserva> findByEstadoStartingWithIgnoreCase(String estado);

}
