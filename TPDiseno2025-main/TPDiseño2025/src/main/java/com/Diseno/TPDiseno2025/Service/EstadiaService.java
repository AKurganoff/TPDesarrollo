package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Model.EstadiaDTO;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;

public interface EstadiaService {
    void crearEstadia(EstadiaDTO eDTO);
    void ocuparHabitacion(List<HabitacionDTO> habitacionesDTO, List<HuespedDTO> huespedesDTO, LocalDate fechaInicio, LocalDate fechaFin, Integer idReservaPrevia);
    Estadia buscarEstadia(EstadiaDTO eDTO);
    Estadia mapToDTO(Estadia estadia, EstadiaDTO eDTO);
}
