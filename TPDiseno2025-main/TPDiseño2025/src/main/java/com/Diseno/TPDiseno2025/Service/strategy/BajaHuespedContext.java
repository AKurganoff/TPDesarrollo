package com.Diseno.TPDiseno2025.Service.strategy;

import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Repository.EstadiaRepository;
import com.Diseno.TPDiseno2025.Repository.ReservaRepository;

@Service
public class BajaHuespedContext {

    private final BajaLogicaStrategy bajaLogica;
    private final BajaFisicaStrategy bajaFisica;
    private final ReservaRepository reservaRepository;
    private final EstadiaRepository estadiaRepository;

    public BajaHuespedContext(
            BajaLogicaStrategy bajaLogica,
            BajaFisicaStrategy bajaFisica,
            ReservaRepository reservaRepository,
            EstadiaRepository estadiaRepository) {
        this.bajaLogica = bajaLogica;
        this.bajaFisica = bajaFisica;
        this.reservaRepository = reservaRepository;
        this.estadiaRepository = estadiaRepository;
    }

    public void darDeBaja(String tipoDni, Integer dni, BajaModo modo) {

        boolean tieneDependencias =
                Boolean.TRUE.equals(reservaRepository.existsByHuesped_DniAndEstadoNotIgnoreCase(dni, "CANCELADA"))
                        || estadiaRepository.existsByReserva_Huesped_Dni(dni);

        if (modo == BajaModo.LOGICA) {
            bajaLogica.darDeBaja(tipoDni, dni);
            return;
        }

        if (modo == BajaModo.FISICA) {
            if (tieneDependencias) {
                throw new IllegalStateException("No se puede baja física: el huésped tiene reservas/estadías asociadas.");
            }
            bajaFisica.darDeBaja(tipoDni, dni);
            return;
        }

        // AUTO
        if (tieneDependencias) bajaLogica.darDeBaja(tipoDni, dni);
        else bajaFisica.darDeBaja(tipoDni, dni);
    }
}
