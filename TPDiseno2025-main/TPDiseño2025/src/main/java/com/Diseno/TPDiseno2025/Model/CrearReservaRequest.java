package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearReservaRequest {

    @Valid
    @NotNull
    private ReservaCrearDTO reserva;

    @Valid
    @NotNull
    private HuespedRefDTO huesped;

    @Valid
    @NotNull
    private HabitacionRefDTO habitacion;
}
