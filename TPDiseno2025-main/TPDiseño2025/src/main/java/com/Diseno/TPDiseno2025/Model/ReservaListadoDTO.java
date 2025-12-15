package com.Diseno.TPDiseno2025.Model;

import java.time.LocalDate;

import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaListadoDTO {
    @NotNull
    private Integer idReserva;

    @NotBlank
    @Size(max = 50)
    private String apellido;
    
    @NotBlank
    @Size(max = 50)
    private String nombre;
    
    @NotBlank
    private Integer numeroHabitacion;
    
    @NotNull
    @NotBlank
    private TipoHabitacion tipoHabitacion;
    
    @NotBlank
    @Size(max = 10)
    private LocalDate fechaInicio;
    
    @NotBlank
    @Size(max = 10)
    private LocalDate fechaFin;
    
}