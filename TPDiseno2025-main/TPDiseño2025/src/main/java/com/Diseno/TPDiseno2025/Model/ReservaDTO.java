package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDTO {

    // En CREATE puede venir null (la BD lo genera). Si viene, que sea > 0.
    @Positive
    private Integer idReserva;

    @NotBlank
    @Size(max = 10)
    // formato YYYY-MM-DD
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "fechaInicio debe tener formato YYYY-MM-DD")
    private String fechaInicio;

    @NotNull
    @Positive
    private Integer cantNoches;

    @NotNull
    @Positive
    private Integer idHuesped;

    @NotNull
    @Positive
    private Integer cantHuesped;

    @NotNull
    private Boolean descuento;

    @NotBlank
    @Size(max = 10)
    private String estado;
}
