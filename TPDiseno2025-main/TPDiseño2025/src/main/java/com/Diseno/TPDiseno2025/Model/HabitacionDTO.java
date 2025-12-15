package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitacionDTO {

    @NotNull
    @Positive
    private Integer idHabitacion;

    @NotNull
    @Positive
    private Integer idTipo;

    @NotNull
    private Integer nochesDescuento;

    @NotBlank
    @Size(max = 50)
    private String estado;
}
