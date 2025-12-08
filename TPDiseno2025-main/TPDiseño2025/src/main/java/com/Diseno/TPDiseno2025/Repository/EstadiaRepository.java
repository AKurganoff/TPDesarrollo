package com.Diseno.TPDiseno2025.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Reserva;

public interface EstadiaRepository extends JpaRepository<Estadia, Integer>{
	boolean existsByReserva(Reserva reserva);
}

