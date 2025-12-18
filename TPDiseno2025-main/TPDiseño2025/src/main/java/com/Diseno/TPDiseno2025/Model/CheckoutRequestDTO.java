package com.Diseno.TPDiseno2025.Model;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequestDTO {

    @NotNull
    private Integer idHabitacion;

    @NotNull
    private LocalTime horaCheckout;
}
