package com.Diseno.TPDiseno2025.Repository;

import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Domain.DetalleReserva;

public interface EstadiaRepository extends JpaRepository<Estadia, Integer>{
	boolean existsByReserva(Reserva reserva);
    
    @Query("""
    select e
    from Estadia e
    join e.reserva r
    join DetalleReserva dr on dr.reserva = r
    join dr.habitacion h
    where h.idHabitacion = :idHabitacion
    and e.horaCheckOut = :horaCheckOut
    """)
	Optional<Estadia> buscarEstadiaPorHabitacionYHora(
        @Param("idHabitacion") Integer idHabitacion,
        @Param("horaCheckOut") LocalTime horaCheckOut
    );
}

