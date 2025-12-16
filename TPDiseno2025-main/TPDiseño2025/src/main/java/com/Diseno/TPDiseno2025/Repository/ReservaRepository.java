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

    Boolean existsByHuesped_DniAndEstadoNotIgnoreCase(Integer dni, String estado);

    List<Reserva> findByEstadoStartingWithIgnoreCase(String estado);

    void deleteById(Integer idReserva);

    List<Reserva> findByHuesped_NombreAndHuesped_ApellidoIgnoreCase(String nombre,String apellido);

    List<Reserva> findByHuesped_ApellidoIgnoreCase(String apellido);
}
