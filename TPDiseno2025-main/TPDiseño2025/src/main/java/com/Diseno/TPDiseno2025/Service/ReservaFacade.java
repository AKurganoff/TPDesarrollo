package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Model.CrearReservaRequest;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservaFacade {

    private final HabitacionService habitacionService;
    private final HuespedService huespedService;
    private final ReservaService reservaService;
    private final DetalleReservaService detalleService;

    public ReservaFacade(HabitacionService habitacionService,
                         HuespedService huespedService,
                         ReservaService reservaService,
                         DetalleReservaService detalleService) {
        this.habitacionService = habitacionService;
        this.huespedService = huespedService;
        this.reservaService = reservaService;
        this.detalleService = detalleService;
    }

    public Integer procesarCrearReserva(CrearReservaRequest req) {

        LocalDate fechaDesde = LocalDate.parse(req.getReserva().getFechaInicio());
        LocalDate fechaHasta = fechaDesde.plusDays(req.getReserva().getCantNoches());

        // Habitación (valida disponibilidad por conflicto de fechas)
        Habitacion habitacion = habitacionService.obtenerHabitacionSiDisponible(
                req.getHabitacion().getIdHabitacion(), fechaDesde, fechaHasta);

        // Huésped existente
        Huesped huesped = huespedService.buscarHuespedByTipoDniAndDni(
                req.getHuesped().getTipoDni(), req.getHuesped().getDni());

        // Reserva
        Reserva nueva = new Reserva();
        nueva.setHuesped(huesped);
        nueva.setCantHuesped(req.getReserva().getCantHuesped());
        nueva.setFechaInicio(fechaDesde);
        nueva.setCantNoches(req.getReserva().getCantNoches());
        nueva.setDescuento(req.getReserva().getDescuento());

        String estado = req.getReserva().getEstado();
        nueva.setEstado((estado == null || estado.isBlank()) ? "Confirmada" : estado);

        Reserva reservaGuardada = reservaService.guardarReserva(nueva);

        // Detalle
        DetalleReserva detalle = new DetalleReserva();
        detalle.setReserva(reservaGuardada);
        detalle.setHabitacion(habitacion);

        double precioTotal = habitacion.getIdTipo().getPrecioNoche() * req.getReserva().getCantNoches();
        detalle.setPrecio(precioTotal);
        detalle.setCantidadNoches(req.getReserva().getCantNoches());

        detalleService.guardarDetalle(detalle);

        return reservaGuardada.getIdReserva();
    }
}
