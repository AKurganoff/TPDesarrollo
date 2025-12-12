package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Model.CeldaCalendarioDTO;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO; 

public interface ReservaService {
    
    void crearReserva(ReservaDTO r, HuespedDTO h, HabitacionDTO habitacion);
    void modificarReserva(Integer idReserva, Reserva rActualizada);
    void eliminarReserva(Reserva r);
    Reserva obtenerReservaPorId(Integer id);
    List<Reserva> obtenerTodas();
    Reserva mapToEntity(ReservaDTO r);
    ReservaDTO mapToDTO(Reserva r);
    List<CeldaCalendarioDTO> obtenerMatrizDisponibilidad(String inicioStr, String finStr, Integer idTipo);
    Reserva guardarReserva(Reserva r);
}
