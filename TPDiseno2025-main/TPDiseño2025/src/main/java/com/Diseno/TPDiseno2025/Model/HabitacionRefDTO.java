package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitacionRefDTO {

    @NotNull
    @Positive
    private Integer idHabitacion;
}
