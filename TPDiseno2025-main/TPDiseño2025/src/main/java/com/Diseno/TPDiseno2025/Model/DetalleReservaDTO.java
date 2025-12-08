package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DetalleReservaDTO {
    
    @NotNull
    private Integer idDetalle;

    @NotNull
    private Integer idHabitacion;

    @NotNull
    private Double precio;

    @NotNull
    private Integer idReserva;
    
}
