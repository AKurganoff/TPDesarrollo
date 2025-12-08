package com.Diseno.TPDiseno2025.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.Huesped;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Integer> {
    
    @Override
    Optional <Huesped> findById(Integer IdHuesped);

    Optional<Huesped> findByTipoDniAndDni(String tipoDni, Integer dni);

    List<Huesped> findByTipoDni(String tipoDni);

    Boolean existsByDireccion(Direccion direccion);

    Boolean existsByDni(Integer dni);

    List<Huesped> findByNombreStartingWithIgnoreCase(String nombre);

    List<Huesped> findByApellidoStartingWithIgnoreCase(String apellido);

    Optional<Huesped> findByNombreAndApellidoAndTipoDniAndDni(
        String nombre,
        String apellido,
        String tipoDni,
        Integer dni
    );

}