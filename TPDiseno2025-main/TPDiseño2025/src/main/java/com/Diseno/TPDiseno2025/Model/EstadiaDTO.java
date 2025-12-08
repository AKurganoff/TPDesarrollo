package com.Diseno.TPDiseno2025.Model;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadiaDTO {
    
    @NotNull
    private Integer idEstadia;

    @NotNull
    private Double precio;

    @NotNull
    private Integer idReserva;

    @NotNull
    private LocalTime horaCheckin;
    
    @NotNull
    private LocalTime horaCheckout;

}
