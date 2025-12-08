package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.List;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;

public interface DetalleReservaService {
    
    void crearDetalle_Reserva(Integer detalle, Reserva r, HabitacionDTO h);
    void eliminarDetalle(Integer idDetalle);
    void darDeBajaHabitacion(Integer idDetalle, Habitacion h);
    DetalleReserva obtenerDetalleByIdReservaAndIdHabitacion(Integer idReserva, Integer idHabitacion);
    DetalleReserva obtenerDetalle(Integer idDetalle);
    List<DetalleReserva> obtenerTodos();
    void guardarDetalle(DetalleReserva dr);
    List<DetalleReserva> buscarReservasEnConflicto(LocalDate fechaInicio, LocalDate fechaFin);
}
