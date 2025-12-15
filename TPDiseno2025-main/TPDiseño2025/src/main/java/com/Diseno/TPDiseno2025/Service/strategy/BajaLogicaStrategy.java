package com.Diseno.TPDiseno2025.Service.strategy;

import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;

@Service
public class BajaLogicaStrategy implements BajaHuespedStrategy {

    private final HuespedRepository huespedRepository;

    public BajaLogicaStrategy(HuespedRepository huespedRepository) {
        this.huespedRepository = huespedRepository;
    }

    @Override
    public void darDeBaja(String tipoDni, Integer dni) {
        Huesped h = huespedRepository.findByTipoDniAndDni(tipoDni, dni)
                .orElseThrow(() -> new IllegalArgumentException("Huésped no encontrado."));

        h.setActivo(false);
        huespedRepository.save(h);
    }
}
