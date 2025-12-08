package com.Diseno.TPDiseno2025.Model;
import lombok.Data;

@Data
public class CeldaCalendarioDTO {
    private String fecha;       // "2025-03-01"
    private Integer idHabitacion;
    private String estado;      // "LIBRE", "OCUPADA", "RESERVADA"
    private Integer idReserva;

    public CeldaCalendarioDTO(){}

    public CeldaCalendarioDTO(String fecha, Integer idHab, String estado, Integer idReserva){
        this.fecha = fecha;
        this.idHabitacion = idHab;
        this.estado = estado;
        this.idReserva = idReserva;
    }
}