package com.Diseno.TPDiseno2025.Model;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class OcupacionRequest {
    private List<HabitacionDTO> habitaciones;
    private List<HuespedDTO> huespedes;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer idReservaPrevia; // Puede ser null si es Walk-in
}