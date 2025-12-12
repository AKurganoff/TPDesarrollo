package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;

public interface HabitacionService {
    
    void crearHabitacion(Integer idHabitacion, Integer idTipo, Integer nochesDesc, String estado);
    HabitacionDTO mapToDTOHabitacion(Habitacion h);
    Habitacion buscarHabitacionByIdHabitacion(Integer idHabitacion);
    HabitacionDTO buscarHabitacionDTOByIdHabitacion(Integer idHabitacion);
    Habitacion mapToEntHabitacion(HabitacionDTO hDto);
    List<Habitacion> obtenerTodas();
    List<Habitacion> obtenerTodasPorTipo(Integer idTipo);
    boolean habitacionDisponibleEnFechas(Integer idHabitacion, LocalDate fechaDesde, LocalDate fechaHasta);
    List<String> mostrarestadoHabitacionesByFecha(String fechaDesde, String fechaHasta);
    Boolean verificarEstadoHabitacion(Integer idHabitacion); 
    void reservarHabitacion(Integer idHabitacion);
    Habitacion obtenerHabitacionSiDisponible(Integer idHabitacion, LocalDate fechaDesde, LocalDate fechaHasta);
}
