package com.Diseno.TPDiseno2025.Service.strategy;

import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;

@Service
public class BajaFisicaStrategy implements BajaHuespedStrategy {

    private final HuespedRepository huespedRepository;

    public BajaFisicaStrategy(HuespedRepository huespedRepository) {
        this.huespedRepository = huespedRepository;
    }

    @Override
    public void darDeBaja(String tipoDni, Integer dni) {
        Huesped h = huespedRepository.findByTipoDniAndDni(tipoDni, dni)
                .orElseThrow(() -> new IllegalArgumentException("Huésped no encontrado."));

        // Si la dirección la usa otro huésped, la desacoplamos para no borrarla por cascade
        Direccion dir = h.getDireccion();
        if (dir != null && Boolean.TRUE.equals(huespedRepository.existsByDireccionAndDniNot(dir, h.getDni()))) {
            h.setDireccion(null);
            huespedRepository.save(h);
        }

        huespedRepository.delete(h);
    }
}
