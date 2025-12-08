package com.Diseno.TPDiseno2025.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Integer>{
    @Override
    Optional<Habitacion> findById(Integer idHabitacion);
    List<Habitacion> findByIdTipo(TipoHabitacion idTipo);
}
