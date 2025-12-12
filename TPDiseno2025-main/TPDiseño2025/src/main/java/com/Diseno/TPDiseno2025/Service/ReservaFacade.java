package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;
import com.Diseno.TPDiseno2025.Service.HabitacionService;
import com.Diseno.TPDiseno2025.Service.HuespedService;
import com.Diseno.TPDiseno2025.Service.ReservaService;
import com.Diseno.TPDiseno2025.Service.DetalleReservaService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservaFacade {
   
    private final HabitacionService habitacionService;
    private final HuespedService huespedService;
    private final ReservaService reservaService;
    private final DetalleReservaService detalleService;

    public ReservaFacade(HabitacionService habitacionService, HuespedService huespedService, ReservaService reservaService, DetalleReservaService detalleService){
        this.habitacionService = habitacionService;
        this.huespedService = huespedService;
        this.reservaService = reservaService;
        this.detalleService = detalleService;
    }

    public void procesarCrearReserva(ReservaDTO r, HuespedDTO h, HabitacionDTO hab){
        
        LocalDate fechaDesde = LocalDate.parse(r.getFechaInicio());
        LocalDate fechaHasta = fechaDesde.plusDays(r.getCantNoches());

        //Habitacion
        Habitacion habitacion = habitacionService.obtenerHabitacionSiDisponible(
            hab.getIdHabitacion(), fechaDesde, fechaHasta);

        //Huesped
        Huesped huesped = huespedService.buscarHuespedByTipoDniAndDni(h.getTipoDni(), h.getDni());

        // crear y guardar reserva
        Reserva nuevaReserva = reservaService.mapToEntity(r);
        nuevaReserva.setHuesped(huesped);

        Reserva reservaGuardada = reservaService.guardarReserva(nuevaReserva);

        // crear y guardar el detalle
        DetalleReserva detalle = new DetalleReserva();
        detalle.setReserva(reservaGuardada);
        detalle.setHabitacion(habitacion);

        //calculo del precio
        double precioTotal = habitacion.getIdTipo().getPrecioNoche() * r.getCantNoches();
        detalle.setPrecio(precioTotal);
        detalle.setCantidadNoches(r.getCantNoches());

        detalleService.guardarDetalle(detalle);
    }
}